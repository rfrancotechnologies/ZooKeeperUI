import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NodeCreationModalComponent } from './node-creation-modal.component';

describe('NodeCreationModalComponent', () => {
  let component: NodeCreationModalComponent;
  let fixture: ComponentFixture<NodeCreationModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NodeCreationModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NodeCreationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
