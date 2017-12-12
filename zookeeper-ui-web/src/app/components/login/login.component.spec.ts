import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { By }              from '@angular/platform-browser';

import { LoginComponent } from './login.component';
import { AlertsComponent } from '../alerts/alerts.component';
import { LoadingSpinnerComponent } from '../loading-spinner/loading-spinner.component';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let routerStub = {};
  let loginServiceStub = {
    logIn: function(username: string, password: string) {}
  };
  let loginService: LoginService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ FormsModule ],
      declarations: [ LoginComponent, AlertsComponent, LoadingSpinnerComponent ],
      providers: [
        { provide: LoginService, useValue: loginServiceStub },
        { provide: Router, useValue: routerStub }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    loginService = fixture.debugElement.injector.get(LoginService);
    fixture.detectChanges();
  });


  it('should log the user in when the login button is pressed', () => {
    let spy = spyOn(loginService, 'logIn');
    component.username = 'TestUserName';
    component.password = 'TestUserPassword';
    fixture.detectChanges();

    let loginButton = fixture.debugElement.query(By.css('button'));
    loginButton.triggerEventHandler('click', null);

    expect(loginService.logIn).toHaveBeenCalledWith('TestUserName', 'TestUserPassword');


  });
});
