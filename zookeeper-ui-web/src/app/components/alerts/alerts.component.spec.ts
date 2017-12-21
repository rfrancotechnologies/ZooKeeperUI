import { async, ComponentFixture, TestBed, fakeAsync, tick  } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { AlertsComponent, AlertList, AlertType, Alert } from './alerts.component';

describe('AlertsComponent', () => {
  let component: AlertsComponent;
  let fixture: ComponentFixture<AlertsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlertsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertsComponent);
    component = fixture.componentInstance;
    component.alerts = new AlertList();
    fixture.detectChanges();
  });

  it('should show an alert div for each alert in the list', () => {
    component.alerts.addAlert(new Alert(AlertType.Error, 'error message'));
    component.alerts.addAlert(new Alert(AlertType.Warning, 'warning message'));
    component.alerts.addAlert(new Alert(AlertType.Success, 'success message'));
    fixture.detectChanges();

    expect(fixture.debugElement.queryAll(By.css('.alert')).length).toBe(3);
  });

  it('should show success alerts with a green background, an info sign and contain the message text', () => {
    component.alerts.addAlert(new Alert(AlertType.Success, 'success message'));
    fixture.detectChanges();

    expect(fixture.debugElement.queryAll(By.css('.alert-success')).length).toBe(1);
    expect(fixture.debugElement.queryAll(By.css('.glyphicon-info-sign')).length).toBe(1);
    expect(fixture.debugElement.query(By.css('.alert')).nativeElement.textContent).toContain('success message');
  });

  it('should show warning alerts with a yellow background, a warning sign and contain the message text', () => {
    component.alerts.addAlert(new Alert(AlertType.Warning, 'warning message'));
    fixture.detectChanges();

    expect(fixture.debugElement.queryAll(By.css('.alert-warning')).length).toBe(1);
    expect(fixture.debugElement.queryAll(By.css('.glyphicon-warning-sign')).length).toBe(1);
    expect(fixture.debugElement.query(By.css('.alert')).nativeElement.textContent).toContain('warning message');
  });

  it('should show error alerts with a red background, an exclamation sign and contain the message text', () => {
    component.alerts.addAlert(new Alert(AlertType.Error, 'error message'));
    fixture.detectChanges();

    expect(fixture.debugElement.queryAll(By.css('.alert-danger')).length).toBe(1);
    expect(fixture.debugElement.queryAll(By.css('.glyphicon-exclamation-sign')).length).toBe(1);
    expect(fixture.debugElement.query(By.css('.alert')).nativeElement.textContent).toContain('error message');
  });

  it('should automatically remove an alert when the specified timeout passes', fakeAsync(() => {
    component.alerts.addAlert(new Alert(AlertType.Error, 'error message', 1000));
    fixture.detectChanges();

    tick(999);
    fixture.detectChanges();
    expect(fixture.debugElement.queryAll(By.css('.alert')).length).toBe(1);

    tick(1);
    fixture.detectChanges();
    expect(fixture.debugElement.queryAll(By.css('.alert')).length).toBe(0);
  }));

  it('should automatically remove an alert after 8 seconds by default', fakeAsync(() => {
    component.alerts.addAlert(new Alert(AlertType.Error, 'error message'));
    fixture.detectChanges();

    tick(7999);
    fixture.detectChanges();
    expect(fixture.debugElement.queryAll(By.css('.alert')).length).toBe(1);

    tick(1);
    fixture.detectChanges();
    expect(fixture.debugElement.queryAll(By.css('.alert')).length).toBe(0);
  }));
});
