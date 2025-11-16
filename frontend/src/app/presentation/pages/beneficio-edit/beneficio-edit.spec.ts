import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BenefioEdit } from './beneficio-edit';

describe('BenefioEdit', () => {
  let component: BenefioEdit;
  let fixture: ComponentFixture<BenefioEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BenefioEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BenefioEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
