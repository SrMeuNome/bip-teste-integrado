import { Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-main-container',
  imports: [],
  templateUrl: './main-container.html',
  styleUrl: './main-container.css',
})
export class MainContainer implements OnInit, OnDestroy {
  private originalHtmlStyles: Partial<CSSStyleDeclaration> = {};
  private originalBodyStyles: Partial<CSSStyleDeclaration> = {};

  ngOnInit(): void {
    const html = document.documentElement;
    const body = document.body;

    this.originalHtmlStyles = {
      margin: html.style.margin,
      padding: html.style.padding,
      height: html.style.height,
      width: html.style.width,
    };

    this.originalBodyStyles = {
      margin: body.style.margin,
      padding: body.style.padding,
      height: body.style.height,
      width: body.style.width,
    };

    html.style.margin = '0';
    html.style.padding = '0';
    html.style.height = '100%';
    html.style.width = '100%';

    body.style.margin = '0';
    body.style.padding = '0';
    body.style.height = '100%';
    body.style.width = '100%';
  }

  ngOnDestroy(): void {
    // Restaura estilos originais
    const html = document.documentElement;
    const body = document.body;

    html.style.margin = this.originalHtmlStyles.margin || '';
    html.style.padding = this.originalHtmlStyles.padding || '';
    html.style.height = this.originalHtmlStyles.height || '';
    html.style.width = this.originalHtmlStyles.width || '';

    body.style.margin = this.originalBodyStyles.margin || '';
    body.style.padding = this.originalBodyStyles.padding || '';
    body.style.height = this.originalBodyStyles.height || '';
    body.style.width = this.originalBodyStyles.width || '';
  }
}
