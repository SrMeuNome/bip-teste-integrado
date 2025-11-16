import { Observable } from "rxjs";
import { Beneficio } from "../models/beneficio.model";
import { CreateBeneficioDTO } from "../dtos/create-beneficio.dto";
import { UpdateBeneficioDTO } from "../dtos/update-beneficio.dto";
import { TransferBeneficioDTO } from "../dtos/transfer-beneficio.dto";

export abstract class BeneficioRepository {
  abstract findAll(): Observable<Beneficio[]>;
  abstract findById(id: number): Observable<Beneficio>;
  abstract create(dto: CreateBeneficioDTO): Observable<Beneficio>;
  abstract update(id: number, dto: UpdateBeneficioDTO): Observable<Beneficio>;
  abstract delete(id: number): Observable<void>;
  abstract transfer(dto: TransferBeneficioDTO): Observable<void>;
}