import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BeneficioService } from '../../../application/service/beneficio-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-beneficio-transfer',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './beneficio-transfer.html',
  styleUrl: './beneficio-transfer.css',
})
export class BeneficioTransfer implements OnInit {
  constructor (
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private beneficioService: BeneficioService
  ) {}

  transferForm!: FormGroup;
  beneficios: any[] = [];
  loading = false;
  submitting = false;

  ngOnInit(): void {
    this.initForm();
    this.loadBeneficios();
    this.setupFormValidations();
  }

  private initForm(): void {
    this.transferForm = this.fb.group({
      fromId: [null, Validators.required],
      toId: [null, Validators.required],
      amount: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  private setupFormValidations(): void {
    this.transferForm.get('toId')?.valueChanges.subscribe(() => {
      this.validateDifferentAccounts();
    });

    this.transferForm.get('fromId')?.valueChanges.subscribe(() => {
      this.validateDifferentAccounts();
      this.validateSufficientFunds();
    });

    this.transferForm.get('amount')?.valueChanges.subscribe(() => {
      this.validateSufficientFunds();
    });
  }

  private validateDifferentAccounts(): void {
    const fromId = this.transferForm.get('fromId')?.value;
    const toId = this.transferForm.get('toId')?.value;
    
    const toIdControl = this.transferForm.get('toId');
    
    if (fromId && toId && fromId === toId) {
      toIdControl?.setErrors({ sameAccount: true });
    } else if (toIdControl?.hasError('sameAccount')) {
      toIdControl?.setErrors(null);
    }
  }

  private validateSufficientFunds(): void {
    const fromId = this.transferForm.get('fromId')?.value;
    const amount = this.transferForm.get('amount')?.value;
    
    const amountControl = this.transferForm.get('amount');
    
    if (fromId && amount) {
      const beneficio = this.beneficios.find(b => b.id === fromId);
      if (beneficio && amount > beneficio.valor) {
        amountControl?.setErrors({ insufficientFunds: true });
      } else if (amountControl?.hasError('insufficientFunds')) {
        amountControl?.setErrors(null);
      }
    }
  }

  private loadBeneficios(): void {
    this.loading = true;
    
    this.beneficioService.findAll().subscribe({
      next: (beneficios) => {
        this.beneficios = beneficios.filter(b => b.ativo);
        this.loading = false;
      },
      error: (error) => {
        this.showError('Erro ao carregar benefícios');
        this.loading = false;
        this.voltar();
      }
    });
  }

  get beneficiosDestino(): any[] {
    const fromId = this.transferForm.get('fromId')?.value;
    if (!fromId) return this.beneficios;
    return this.beneficios.filter(b => b.id !== fromId);
  }

  get showSummary(): boolean {
    const fromId = this.transferForm.get('fromId')?.value;
    const toId = this.transferForm.get('toId')?.value;
    const amount = this.transferForm.get('amount')?.value;
    
    return fromId && toId && amount > 0;
  }

  getSelectedBeneficioName(type: 'from' | 'to'): string {
    const id = type === 'from' 
      ? this.transferForm.get('fromId')?.value 
      : this.transferForm.get('toId')?.value;
    
    const beneficio = this.beneficios.find(b => b.id === id);
    return beneficio ? beneficio.nome : '';
  }

  onSubmit(): void {
    if (this.transferForm.invalid) {
      this.transferForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const dto = this.transferForm.value;
    
    this.beneficioService.transfer(dto).subscribe({
      next: () => {
        this.showSuccess('Transferência realizada com sucesso!');
        this.voltar();
      },
      error: (error) => {
        this.showError('Erro ao realizar transferência');
        this.submitting = false;
      }
    });
  }

  voltar(): void {
    this.router.navigate(['/']); // Ajuste a rota conforme necessário
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Fechar', {
      duration: 4000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }
}
