import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UploadDocComponent } from './upload-doc/upload-doc.component';
import { HttpClientModule } from '@angular/common/http';
import { ToastService, AngularToastifyModule } from 'angular-toastify'; 

@NgModule({
  declarations: [
    AppComponent,
    UploadDocComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AngularToastifyModule,
  ],
  providers: [ToastService],
  bootstrap: [AppComponent]
})
export class AppModule { }
