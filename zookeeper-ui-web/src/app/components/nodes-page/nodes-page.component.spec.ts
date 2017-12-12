import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NodesPageComponent } from './nodes-page.component';

describe('NodesPageComponent', () => {
  let component: NodesPageComponent;
  let fixture: ComponentFixture<NodesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NodesPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NodesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
});
