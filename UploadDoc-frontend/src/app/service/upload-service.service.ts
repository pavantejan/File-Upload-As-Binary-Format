import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


const api = "http://localhost:8080";


@Injectable({
  providedIn: 'root'
})
export class UploadServiceService {

  constructor(private http:HttpClient) { }


  upload(formData:FormData): Observable < any > {  
    return this.http.post( api + "/save-pdf", formData,
      { responseType: 'text' }
    );
  }

  download(name:string): Observable<Blob>{
    return this.http.get(api + "/download/"+`${name}`,
      { responseType: 'blob' }
    );
  }

  getAllFiles():Observable<any>{
    return this.http.get(api+"/getAll",
      { responseType:'json' }
    );
  }

  deleteDoc(name:string):Observable<any>{
    return this.http.delete(api+"/remove/"+`${name}`,
      { responseType: 'text' }
    )
  }

}
