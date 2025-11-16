import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BenefioView } from './beneficio-view';

describe('BenefioView', () => {
  let component: BenefioView;
  let fixture: ComponentFixture<BenefioView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BenefioView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BenefioView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
