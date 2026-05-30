// js/api.js 
const API = '/api'; 
  
async function apiGet(path) { 
  const res = await fetch(API + path, { credentials: 'include' }); 
  if (!res.ok) throw new Error('Erro: ' + res.status); 
  return res.json(); 
} 
  
async function apiPost(path, body) { 
  const res = await fetch(API + path, { 
    method: 'POST', 
    headers: { 'Content-Type': 'application/json' }, 
    credentials: 'include', 
    body: JSON.stringify(body) 
  }); 
  const data = await res.json().catch(() => ({})); 
  if (!res.ok) throw new Error(data.mensagem || ('Erro: ' + res.status)); 
  return data; 
} 
  
async function apiPut(path, body) { 
  const res = await fetch(API + path, { 
    method: 'PUT', 
    headers: { 'Content-Type': 'application/json' }, 
    credentials: 'include', 
    body: JSON.stringify(body) 
  }); 
  const data = await res.json().catch(() => ({})); 
  if (!res.ok) throw new Error(data.mensagem || ('Erro: ' + res.status)); 
  return data; 
} 
  
async function apiDelete(path) { 
  const res = await fetch(API + path, { 
    method: 'DELETE', 
    credentials: 'include' 
  }); 
  if (!res.ok && res.status !== 204) throw new Error('Erro: ' + res.status); 
} 
  
async function usuarioLogado() { 
  try { return await apiGet('/auth/me'); } 
  catch { return null; } 
}