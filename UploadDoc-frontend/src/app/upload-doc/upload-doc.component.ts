import { Component, Input, ChangeDetectorRef, OnInit } from '@angular/core';
import { UploadServiceService } from '../service/upload-service.service';
import { ToastService } from 'angular-toastify';



@Component({
  selector: 'app-upload-doc',
  templateUrl: './upload-doc.component.html',
  styleUrls: ['./upload-doc.component.css']
})
export class UploadDocComponent implements OnInit{

  @Input() file: File | null = null;
  docNames: any[] = [];

  constructor(
    private service: UploadServiceService,
    private _toastService: ToastService,
    private cdr: ChangeDetectorRef
    ){}

  ngOnInit(): void {
    this.getAllFiles();
  }

  

  uploadFile(e:any){

    this.file = e.target.files[0];
    console.log("inside upload file");
    const formData = new FormData();
    if (this.file) {
      formData.append('file', this.file, this.file.name);
      console.log("form data updated");
    }  

    if (formData) {
      console.log("post request");
      this.service.upload(formData).subscribe({
        next: (data:any) => {
          console.log("file uploaded data :", data);
          this.docNames.push(this.file?.name);
          this._toastService.success(data);
        },
        error: (error)=>{
          console.log("file uploaded error :", error.error);
          this._toastService.error(error.error);
        }
    });
    }else{
      console.log("formdata is not created");
    }

    // this.getAllFiles();
    // this.cdr.detectChanges();    
  }

  getAllFiles(){
    this.service.getAllFiles().subscribe({
      next: (data:any)=>{
        this.docNames = data;
        console.log("data for get all doc names");
        console.log(data);
        
      },
      error: (error)=>{
        console.log(error);
      }
    })
  }

  downloadFile(name:string){
    // console.log(name);
    this.service.download(name).subscribe(
      blob =>{
        console.log("blob file received");
        console.log(blob);
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = name;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      });
  }

  removeFile(name:string){
    this.service.deleteDoc(name).subscribe({
      next: (data)=>{
      this.docNames = this.docNames.filter((val)=>val !== name);

      this._toastService.success(data);
      },
      error: (error)=>{
        this._toastService.error(error.error);
      }
    });
    // this.getAllFiles();
    // this.cdr.detectChanges();
  }








}
