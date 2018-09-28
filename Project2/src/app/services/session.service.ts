import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class SessionService {
  constructor() { }

  sessionCheck(): boolean {
    if(localStorage.length > 0) {
      return true;
    } else { 
      return false;
    }
  }

  legendCheck(): boolean {
    if(localStorage.getItem("role") === "n") {
      return false;
    } else {
      return true;
    }
  }
}
