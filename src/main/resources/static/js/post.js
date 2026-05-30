// js/posts.js 
let paginaAtual = 0; 
let termoBusca = ''; 
  
async function carregarPosts(pagina = 0) { 
  const params = new URLSearchParams(); 
  params.set('page', pagina); 
  params.set('size', 10); 
  if (termoBusca) params.set('busca', termoBusca); 
  
  try { 
    const data = await apiGet('/posts?' + params.toString()); 
    renderizar(data); 
  } catch (e) { 
    document.getElementById('listaPosts').innerHTML = 
      `<div class="alert alert-danger">${e.message}</div>`; 
  } 
} 
  
function renderizar(page) { 
  const lista = document.getElementById('listaPosts'); 
  if (page.content.length === 0) { 
    lista.innerHTML = '<div class="alert alert-info">Nenhum post encontrado.</div>'; 
    return; 
  } 
  lista.innerHTML = page.content.map(p => ` 
    <article class="card mb-3 shadow-sm"> 
      <div class="card-body"> 
        <h2 class="h4"> 
          <a href="/post.html?slug=${p.slug}" class="text-decoration-none"> 
            ${escapar(p.titulo)} 
          </a> 
        </h2> 
        <p class="text-muted small mb-2"> 
          Por ${escapar(p.autor.nome)} • ${formatar(p.criadoEm)} • 
          <i class="bi bi-chat"></i> ${p.totalComentarios} 
        </p> 
        <p>${escapar(p.resumo)}</p> 
        <div> 
          ${[...p.tags].map(t => `<span class="badge bg-secondary me
1">${escapar(t)}</span>`).join('')} 
        </div> 
      </div> 
    </article> 
  `).join(''); 
  renderizarPaginacao(page); 
} 
  
function renderizarPaginacao(page) { 
  const pag = document.getElementById('paginacao'); 
  if (page.totalPages <= 1) { pag.innerHTML = ''; return; } 
  let html = ''; 
  for (let i = 0; i < page.totalPages; i++) { 
    const ativo = i === page.number ? 'active' : ''; 
    html += `<li class="page-item ${ativo}"> 
      <button class="page-link" onclick="carregarPosts(${i})">${i + 1}</button></li>`; 
  } 
  pag.innerHTML = html; 
} 
  
function escapar(t) { 
  const d = document.createElement('div'); 
  d.textContent = t || ''; 
  return d.innerHTML; 
} 
  
function formatar(iso) { 
  return new Date(iso).toLocaleDateString('pt-BR'); 
} 
  
document.addEventListener('DOMContentLoaded', () => { 
  document.getElementById('btnBuscar').addEventListener('click', () => { 
    termoBusca = document.getElementById('busca').value.trim(); 
    carregarPosts(0); 
  }); 
  carregarPosts(0); 
});