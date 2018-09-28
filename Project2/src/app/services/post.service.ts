import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { Team } from '../models/team';
//import { ResponseOptions } from '@angular/http';
@Injectable()
export class PostService {
  urlAll: string = "https://raw.githubusercontent.com/MaxEffect13/project2/master/SuperheroesWorkspace/src/main/resources/heroes.json";
  urlHero: string = "http://www.superheroapi.com/api.php/10217180226358754/"+window.location.search.substring(4);
  url: string = "http://192.168.0.35:8084/";
  constructor(private http: HttpClient) {}

  getHeroes(): Promise<Object> {
    //console.log(JSON.stringify(this.http.get<Hero[]>(this.url)));
    console.log(this.http.get<Object>(this.urlAll).toPromise());
    return this.http.get<Object>(this.urlAll).toPromise();

    }

  getHero(id: string): Promise<Object> {

    return this.http.get<Object>(this.urlHero).toPromise();
  }

  login(usr: User): Observable<User> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      user: usr.username,
      pass: usr.password
    }
    console.log(this.http.post<any>(this.url + "login", body, {headers}));
    return this.http.post<any>(this.url + "login", body, {headers});
  }

  register(usr: User): Observable<User> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      username: usr.username,
      password: usr.password,
      email: usr.email
    }
    return this.http.post<any>(this.url + "user/create", body, {headers});
  }

  getTeams(): Promise<Object> {
    return this.http.get<Object>(this.url + "team/all").toPromise();
  }

  getUserTeams(id: string): Promise<Team> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      userId: +(localStorage.getItem("userID"))
    }
    return this.http.post<Team>(this.url + "team/byuser", body, { headers }).toPromise();
  }
  createTeam(teamName: string): Observable<Object> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      teamName: teamName,
      userId: +(localStorage.getItem("userID"))
    }
    return this.http.post<any>(this.url + "team/create", body, { headers });
  }

  addToTeam(teamId: number, heroId: number, userId: string): Observable<Object> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      teamId: +(teamId),
      heroId: +(heroId),
      userId: +(userId)
    }
    return this.http.post<any>(this.url + "team/addhero", body, { headers });
  }

  battle(): Promise<Team> {
    return this.http.get<any>(this.url + "combat?teamId1=" + localStorage.getItem("team1") + "&teamId2=" + localStorage.getItem("team2")).toPromise();
  }

  edit(email: string, password: string): Observable<Object> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      username: localStorage.getItem("username"),
      password: password,
      email: email,
      role: localStorage.getItem("role")
    }
    return this.http.post<any>(this.url + "user/update", body, { headers });
  }

  makeLegend(): Observable<Object> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      username: localStorage.getItem("username"),
      password: localStorage.getItem("password"),
      email: localStorage.getItem("email"),
      role: 'y'
    }
    return this.http.post<any>(this.url + "user/update", body, { headers });
  }

  removeTeam(id: number): Observable<Object> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    var body = {
      userId: +(localStorage.getItem("userID")),
      teamId: id
    }
    return this.http.post<any>(this.url + "team/remove", body, { headers });
  }
}