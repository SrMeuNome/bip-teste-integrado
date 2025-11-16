import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { BeneficioService } from '../../../application/service/beneficio-service';

@Component({
  selector: 'beneficio-edit',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSlideToggleModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './beneficio-edit.html',
  styleUrl: './beneficio-edit.css',
})
export class BenefioEdit implements OnInit {
    constructor (
      private fb: FormBuilder,
      private route: ActivatedRoute,
      private router: Router,
      private snackBar: MatSnackBar,
      private beneficioService: BeneficioService,
    ) {}

  beneficioForm!: FormGroup;
  isEditMode = false;
  beneficioId?: number;
  loading = false;
  submitting = false;

  ngOnInit(): void {
    this.initForm();
    this.checkEditMode();
  }

  private initForm(): void {
    this.beneficioForm = this.fb.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
      descricao: ['', [Validators.required, Validators.maxLength(500)]],
      valor: [0, [Validators.required, Validators.min(0)]],
      ativo: [true]
    });
  }

  private checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    
    if (id) {
      this.isEditMode = true;
      this.beneficioId = +id;
      this.loadBeneficio(this.beneficioId);
    }
  }

  private loadBeneficio(id: number): void {
    this.loading = true;
    
    this.beneficioService.findById(id).subscribe({
      next: (beneficio) => {
        this.beneficioForm.patchValue({
          nome: beneficio.nome,
          descricao: beneficio.descricao,
          valor: beneficio.valor,
          ativo: beneficio.ativo
        });
        this.loading = false;
      },
      error: (error) => {
        this.showError('Erro ao carregar benefício');
        console.error(error);
        this.loading = false;
        this.voltar();
      }
    });
  }

  onSubmit(): void {
    if (this.beneficioForm.invalid) {
      this.beneficioForm.markAllAsTouched();
      return;
    }

    this.submitting = true;

    if (this.isEditMode && this.beneficioId) {
      this.updateBeneficio();
    } else {
      this.createBeneficio();
    }
  }

  private createBeneficio(): void {
    const dto = this.beneficioForm.value;
    this.beneficioService.create(dto).subscribe({
      next: (beneficio) => {
        this.showSuccess('Benefício criado com sucesso!');
        this.voltar();
      },
      error: (error) => {
        this.showError('Erro ao criar benefício');
        console.error(error);
        this.submitting = false;
      }
    });
  }

  private updateBeneficio(): void {
    const dto = this.beneficioForm.value;
    
    this.beneficioService.update(this.beneficioId!, dto).subscribe({
      next: (beneficio) => {
        this.showSuccess('Benefício atualizado com sucesso!');
        this.voltar();
      },
      error: (error) => {
        this.showError('Erro ao atualizar benefício');
        console.error(error);
        this.submitting = false;
      }
    });
  }

  voltar(): void {
    this.router.navigate(['/']);
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
