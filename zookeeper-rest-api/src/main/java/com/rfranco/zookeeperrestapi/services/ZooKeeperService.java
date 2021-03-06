package com.rfranco.zookeeperrestapi.services;

import com.rfranco.zookeeperrestapi.autogenerated.dtos.NodeCreationRequest;
import com.rfranco.zookeeperrestapi.autogenerated.dtos.NodeDataType;
import com.rfranco.zookeeperrestapi.autogenerated.dtos.NodeExport;
import com.rfranco.zookeeperrestapi.exceptions.ForbiddenException;
import com.rfranco.zookeeperrestapi.exceptions.NodeAlreadyExistsException;
import com.rfranco.zookeeperrestapi.exceptions.NotFoundException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.*;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.*;

import java.util.List;

/**
 * Created by ruben.martinez on 21/11/2017.
 */
@Service
public class ZooKeeperService {
    @Autowired
    private CuratorFramework zooKeeperClient;

    public List<String> getNodeChildren(String path) throws Exception {
        try {
            GetChildrenBuilder builder = zooKeeperClient.getChildren();
            List<String> children = builder.forPath(path);
            if (path.equals("/")) {
                children.remove("zookeeper");
                children.remove(".node_data_types");
            }
            return children;
        } catch(KeeperException ex) {
            if (ex.code() == KeeperException.Code.NONODE)
                throw new NotFoundException("No node was found at the " + path + " path.", ex);
            else
                throw ex;
        }
    }

    public String getNodeData(String path) throws Exception {
        try {
            return new String(zooKeeperClient.getData().forPath(path), "UTF-8");
        } catch(KeeperException ex) {
            if (ex.code() == KeeperException.Code.NONODE)
                throw new NotFoundException("No node was found at the " + path + " path.", ex);
            else
                throw ex;
        }
    }

    public void setNodeData(String path, String value) throws Exception {
        if (path.equals("/") || path.equals("/zookeeper"))
            throw new ForbiddenException("The specified node is read only");
        try {
            zooKeeperClient.setData().forPath(path, value.getBytes("UTF-8"));
        } catch(KeeperException ex) {
            if (ex.code() == KeeperException.Code.NONODE)
                throw new NotFoundException("No node was found at the " + path + " path.", ex);
            else
                throw ex;
        }
    }

    public void deleteNode(String path) throws Exception {
        if (path.equals("/") || path.equals("/zookeeper"))
            throw new ForbiddenException("The specified node cannot be deleted");

        try {
            zooKeeperClient.delete().deletingChildrenIfNeeded().forPath(path);
        } catch(KeeperException ex) {
            if (ex.code() == KeeperException.Code.NONODE)
                throw new NotFoundException("No node was found at the " + path + " path.", ex);
            else
                throw ex;
        }
    }

    public void addNode(String path, NodeCreationRequest body) throws Exception {
        if (!path.endsWith("/"))
            path += "/";
        path += body.getName();

        String data =  body.getData() != null ?  body.getData() : "";
        try {
            zooKeeperClient.transaction().forOperations(
                    zooKeeperClient.transactionOp().create().forPath(path, data.getBytes("UTF-8"))
            );
        } catch(KeeperException ex) {
            if (ex.code() == KeeperException.Code.NONODE)
                throw new NotFoundException("No node was found at the " + path + " path.", ex);
            else if (ex.code() == KeeperException.Code.NODEEXISTS)
                throw new NodeAlreadyExistsException("A node with path " + path + " already exists.", ex);
            else
                throw ex;
        }
    }

    public NodeExport getNodeExport(String path) throws Exception {
        try {
            NodeExport export = new NodeExport()
                    .name(ZKPaths.getNodeFromPath(path))
                    .value(getNodeData(path));
            List<String> children = getNodeChildren(path);
            for (String childName: children) {
                export.addChildrenItem(getNodeExport(ZKPaths.fixForNamespace(path, "/" + childName)));
            }
            return export;
        } catch(KeeperException ex) {
            if (ex.code() == KeeperException.Code.NONODE)
                throw new NotFoundException("No node was found at the " + path + " path.", ex);
            else
                throw ex;
        }
    }

    public void restoreNodeExport(String path, NodeExport node, Boolean prune, Boolean overwrite) throws Exception{
        try {
            if (zooKeeperClient.checkExists().forPath(path) != null) {
                if (overwrite)
                    zooKeeperClient.setData().forPath(path, node.getValue().getBytes("UTF-8"));
            } else {
                zooKeeperClient.create().forPath(path, node.getValue().getBytes("UTF-8"));
            }

            if (prune) {
                List<String> alreadyExistingChildren =  getNodeChildren(path);
                String[] existingChildrenNotInExport = alreadyExistingChildren.stream().filter(
                        alreadyExistingChild -> !node.getChildren().stream().anyMatch(
                                importedNodeChild -> importedNodeChild.getName().equals(alreadyExistingChild))
                ).toArray(size -> new String[size]);

                for (String childNotInExport: existingChildrenNotInExport) {
                    deleteNode(ZKPaths.fixForNamespace(path, "/" + childNotInExport));
                }
            }

            for (NodeExport childNode: node.getChildren()) {
                restoreNodeExport(ZKPaths.fixForNamespace(path, "/" + childNode.getName()), childNode, prune, overwrite);
            }
        } catch(KeeperException ex) {
            if (ex.code() == KeeperException.Code.NONODE)
                throw new NotFoundException("No node was found at the " + path + " path.", ex);
            else
                throw ex;
        }
    }

    public NodeDataType getNodeDataType(String path) throws Exception {
        try {
            return NodeDataType.fromValue(getNodeData("/.node_data_types" + path));
        } catch(NotFoundException ex) {
            return NodeDataType.STRING;
        }
    }

    public void setNodeDataType(String path, NodeDataType nodeDataType) throws Exception {
        try {
            setNodeData("/.node_data_types" + path, nodeDataType.toString());
        } catch(NotFoundException ex) {
            zooKeeperClient.create().creatingParentsIfNeeded().forPath("/.node_data_types" + path, nodeDataType.toString().getBytes("UTF-8"));
        }
    }
}
