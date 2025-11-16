import { Component, Input } from '@angular/core';
import { ThemeVariant } from '../../../application/interfaces/components';

@Component({
  selector: 'app-top-bar',
  imports: [],
  templateUrl: './top-bar.html',
  styleUrl: './top-bar.css',
})
export class TopBar {
  @Input() variant: ThemeVariant = 'primary';
  @Input() leftClass: string = '';
  @Input() centerClass: string = '';
  @Input() rightClass: string = '';
  @Input() className: string = '';
  @Input() responsive: boolean = false;
}
