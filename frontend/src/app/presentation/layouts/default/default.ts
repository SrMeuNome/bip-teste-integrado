import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TopBar } from '../../components/top-bar/top-bar';
import { MainContainer } from '../../components/main-container/main-container';

@Component({
  selector: 'app-default',
  imports: [RouterModule, MainContainer, TopBar],
  templateUrl: './default.html',
  styleUrl: './default.css',
})
export class Default {

}
