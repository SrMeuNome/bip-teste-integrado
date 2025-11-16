import { Component } from '@angular/core';
import { Beneficio } from '../../../../domain/models/beneficio.model';
import { BeneficioService } from '../../../application/service/beneficio-service';
import { Router } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { CLIENT_ROUTES } from '../../../../domain/constants/client-routes';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../../components/confirm-dialog/confirm-dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-beneficio-view',
  imports: [CurrencyPipe, MatTableModule, MatIconModule, MatButtonModule],
  templateUrl: './beneficio-view.html',
  styleUrl: './beneficio-view.css',
})
export class BenefioView {
  beneficios: Beneficio[] = [];
  displayedColumns: string[] = ["id", "nome", "descricao", "valor", "status", "editar", "excluir"]
  loading = false;
  errorMessage = '';

  constructor(
    private beneficioService: BeneficioService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.carregarBeneficios();
  }

  carregarBeneficios(): void {
    this.loading = true;
    this.errorMessage = '';
    
    this.beneficioService.findAll().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar benefícios';
        this.loading = false;
        console.error(error)
      }
    });
  }

  editar(id: number): void {
    this.router.navigate([CLIENT_ROUTES.BENEFICIOS_EDIT, id]);
  }

  confirmarExclusao(element: Beneficio) {
  const dialogRef = this.dialog.open(ConfirmDialog, {
    width: '350px',
    data: {
      mensagem: `Tem certeza que deseja excluir o benefício "${element.nome}"?`
    }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.deletar(element.id);
    }
  });
}

  deletar(id: number): void {
    this.beneficioService.delete(id).subscribe({
      next: () => {
        this.carregarBeneficios();
      },
      error: (error) => {
        this.errorMessage = error;
      }
    });
  }

  novo(): void {
    this.router.navigate([CLIENT_ROUTES.BENEFICIO_CREATE]);
  }

  irParaTransferencia(): void {
    this.router.navigate([CLIENT_ROUTES.BENEFICIO_TRANSFER]);
  }
}
