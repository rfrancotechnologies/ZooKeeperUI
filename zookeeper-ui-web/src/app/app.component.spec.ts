import { TestBed, async, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { LoginService } from './services/login.service';
import { Subject } from 'rxjs';

import { RouterOutletStubComponent } from './testing/router-stubs';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let loginService: LoginService;

  let loginServiceStub = {
    loggedInObservable: new Subject<boolean>()
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        AlertsComponent,
        RouterOutletStubComponent
      ],
      providers: [
        { provide: LoginService, useValue: loginServiceStub }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    loginService = fixture.debugElement.injector.get(LoginService);
  });

  it('should show the log out button only when the user is logged in', fakeAsync(() => {
    loginServiceStub.loggedInObservable.next(false);
    tick();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.logout-button-selector'))).toBeNull();

    loginServiceStub.loggedInObservable.next(true);
    tick();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.logout-button-selector'))).not.toBeNull();
  }));
});
