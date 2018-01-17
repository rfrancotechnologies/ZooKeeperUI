import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { FileUploadModalComponent } from './file-upload-modal.component';
import { AlertsComponent } from '../alerts/alerts.component';

describe('FileUploadModalComponent', () => {
  let component: FileUploadModalComponent;
  let fixture: ComponentFixture<FileUploadModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ FormsModule ],
      declarations: [ FileUploadModalComponent, AlertsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileUploadModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should save the chosen file when selected', () => {
    let fileInput = fixture.debugElement.query(By.css('#file'));
    fileInput.triggerEventHandler('change', { 'srcElement': { 'files': ['testfile.json'] } });
    expect(component.file).not.toBeUndefined();
  });

  it('should emit event when the selected file is loaded', (done) => {
    component.onFileLoaded.subscribe((event) => {
      done();
    });
    component.onLoad({ 'target': { 'result': "{}" } });
  });

  it('should indicate that it should prune nodes when the selected file is loaded if configured to do so', (done) => {
    component.prune = true;
    component.onFileLoaded.subscribe((event) => {
      expect(event.prune).toBeTruthy();
      done();
    });
    setTimeout(component.onLoad({ 'target': { 'result': "{}" } }),1000);
  });

  it('should indicate that it should not prune nodes when the selected file is loaded if configured to do so', (done) => {
    component.prune = false;
    component.onFileLoaded.subscribe((event) => {
      expect(event.prune).toBeFalsy();
      done();
    });
    component.onLoad({ 'target': { 'result': "{}" } });
  });

  it('should indicate that it should overwrite nodes when the selected file is loaded if configured to do so', (done) => {
    component.overwriteValues = true;
    component.onFileLoaded.subscribe((event) => {
      expect(event.overwriteValues).toBeTruthy();
      done();
    });
    component.onLoad({ 'target': { 'result': "{}" } });
  });

  it('should indicate that it should not overwrite nodes when the selected file is loaded if configured to do so', (done) => {
    component.overwriteValues = false;
    component.onFileLoaded.subscribe((event) => {
      expect(event.overwriteValues).toBeFalsy();
      done();
    });
    component.onLoad({ 'target': { 'result': "{}" } });
  });
});
