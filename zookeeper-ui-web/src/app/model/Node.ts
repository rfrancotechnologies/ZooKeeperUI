export class Node {
  public path: string;
  public name: string;
  public value: string;
  private _parent: Node;
  private _children: Node[];

  public static ROOT_NODE = new Node('~', 'Root');

  constructor(path: string, name: string, parent?: Node) {
    this.path = path;
    this.name = name;
    this._parent = parent;
    this._children = [];
  }

  get nodeParents(): Node[] {
    var parents: Node[] = [];
    var node: Node = this;
    do {
      parents.push(node);
      node = node._parent;
    } while (node);
    return parents.reverse();
  }

  private getChildNodePath(child: string): string {
    var result: string = this.path;
    if (!result.endsWith('~'))
      result += '~';
    result += child;
    return result;
  }

  addChildNode(child: string): Node {
    var childNode = new Node(this.getChildNodePath(child), child, this);
    this._children.push(childNode);
    this._children.sort((a, b) => a.name.localeCompare(b.name));
    return childNode;
  }

  clearChildren(): void {
    this._children = [];
  }

  get hasChildren(): boolean {
    return this._children.length > 0;
  }

  get children(): Node[] {
    return this._children;
  }

  delete(): void {
    var i = this._parent.children.indexOf(this);
    if(i != -1) {
      this._parent.children.splice(i, 1);
    }
  }
}
