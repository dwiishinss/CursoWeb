import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { NgModel } from '@angular/forms';
import { MessageModule } from 'primeng/message';

@Component({
  selector: 'app-message',
  standalone: true,
  imports: [MessageModule, CommonModule],
  template: `
    <p-message *ngIf="temErro()" severity="error" text="{{text}}"></p-message>              
  `,
  styles: ``
})
export class MessageComponent {

  @Input() error?: string;
  @Input() control?: NgModel;
  @Input() text?: string;

  temErro() : boolean {
    return !!(this.control?.hasError(this.error || '') && this.control?.dirty)
  }

}
