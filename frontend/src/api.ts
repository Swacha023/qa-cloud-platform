const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

async function request<T>(path:string, options:RequestInit = {}):Promise<T>{
  const token=localStorage.getItem('qa_token');
  const headers=new Headers(options.headers);
  headers.set('Content-Type','application/json');
  if(token) headers.set('Authorization',`Bearer ${token}`);
  const res=await fetch(`${BASE}${path}`,{...options,headers});
  if(!res.ok){const data=await res.json().catch(()=>({})); throw new Error(data.error||`Request failed (${res.status})`)}
  return res.json();
}
export type Project={id:number;name:string;description:string};
export type Suite={id:number;name:string;description:string;testCases?:Case[]};
export type Case={id:number;title:string;description:string;steps:string;expectedResult:string;priority:string};
export type Bug={id:number;title:string;description:string;severity:string;status:string;assignee?:string};
export type Run={id:number;status:string;total:number;passed:number;failed:number;skipped:number};
export const api={
 login:(email:string,password:string)=>request<{token:string;email:string;displayName:string}>('/auth/login',{method:'POST',body:JSON.stringify({email,password})}),
 dashboard:()=>request<Record<string,number>>('/dashboard'),
 projects:()=>request<Project[]>('/projects'),
 createProject:(name:string,description:string)=>request<Project>('/projects',{method:'POST',body:JSON.stringify({name,description})}),
 suites:(projectId:number)=>request<Suite[]>(`/projects/${projectId}/suites`),
 createSuite:(projectId:number,name:string,description:string)=>request<Suite>(`/projects/${projectId}/suites`,{method:'POST',body:JSON.stringify({name,description})}),
 cases:(suiteId:number)=>request<Case[]>(`/suites/${suiteId}/cases`),
 createCase:(suiteId:number,payload:object)=>request<Case>(`/suites/${suiteId}/cases`,{method:'POST',body:JSON.stringify(payload)}),
 runs:(projectId:number)=>request<Run[]>(`/projects/${projectId}/runs`),
 startRun:(projectId:number)=>request<Run>(`/projects/${projectId}/runs`,{method:'POST'}),
 bugs:(projectId:number)=>request<Bug[]>(`/projects/${projectId}/bugs`),
 createBug:(projectId:number,payload:object)=>request<Bug>(`/projects/${projectId}/bugs`,{method:'POST',body:JSON.stringify(payload)}),
};
