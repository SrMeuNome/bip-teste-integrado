import { Injectable } from '@angular/core';
import { BeneficioRepository } from '../../../domain/repositories/beneficio.repository';
import { Observable } from 'rxjs';
import { Beneficio } from '../../../domain/models/beneficio.model';
import { CreateBeneficioDTO, TransferBeneficioDTO, UpdateBeneficioDTO } from '../../../domain/dtos';

@Injectable({
  providedIn: 'root',
})
export class BeneficioService {
  constructor(private repository: BeneficioRepository) {}

  findAll(): Observable<Beneficio[]> {
    return this.repository.findAll();
  }

  findById(id: number): Observable<Beneficio> {
    return this.repository.findById(id);
  }

  create(dto: CreateBeneficioDTO): Observable<Beneficio> {
    return this.repository.create(dto);
  }

  update(id: number, dto: UpdateBeneficioDTO): Observable<Beneficio> {
    return this.repository.update(id, dto);
  }

  delete(id: number): Observable<void> {
    return this.repository.delete(id);
  }

  transfer(dto: TransferBeneficioDTO): Observable<void> {
    return this.repository.transfer(dto);
  }
}
