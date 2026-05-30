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
          <a href="#" onclick="abrirPost(event, '${p.slug}')" class="text-decoration-none"> 
            ${escapar(p.titulo)} 
          </a> 
        </h2> 
        <p class="text-muted small mb-2"> 
          Por ${escapar(p.autor.nome)} • ${formatar(p.criadoEm)} • 
          <i class="bi bi-chat"></i> ${p.totalComentarios} 
        </p> 
        <p>${escapar(p.resumo)}</p> 
        <div> 
          ${[...p.tags].map(t => `<span class="badge bg-secondary me-1">${escapar(t)}</span>`).join('')} 
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

// ==========================================
// NOVAS FUNÇÕES: LER O POST NA MESMA PÁGINA
// ==========================================

async function abrirPost(event, slug) {
    event.preventDefault(); // Impede o navegador de subir a página pro topo

    // 1. Esconde a lista, a paginação e a barra de busca
    document.getElementById('areaBusca').style.display = 'none';
    document.getElementById('listaPosts').style.display = 'none';
    document.getElementById('paginacao').style.display = 'none';

    // 2. Mostra a área do post com um "Carregando"
    const postContainer = document.getElementById('postCompleto');
    postContainer.style.display = 'block';
    postContainer.innerHTML = '<div class="text-center p-5"><div class="spinner-border text-primary"></div></div>';

    try {
        // 3. Busca o post completo no Java
        const post = await apiGet('/posts/' + slug);
        const tagsHtml = post.tags.map(t => `<span class="badge bg-secondary me-1">${escapar(t)}</span>`).join('');

        // 4. Renderiza o HTML do post
        postContainer.innerHTML = `
            <div class="card shadow-sm mb-4">
                <div class="card-body p-4 p-md-5">
                    <button class="btn btn-sm btn-outline-secondary mb-4" onclick="voltarParaLista()">
                        <i class="bi bi-arrow-left"></i> Voltar para os posts
                    </button>
                    
                    <h1 class="card-title fw-bold mb-3">${escapar(post.titulo)}</h1>
                    
                    <div class="text-muted mb-4 pb-3 border-bottom">
                        <strong>Autor:</strong> ${escapar(post.autor.nome)} &nbsp;|&nbsp; 
                        <strong>Publicado em:</strong> ${formatar(post.criadoEm)}
                    </div>
                    
                    <div class="mb-5 fs-5" style="white-space: pre-wrap;">${escapar(post.conteudo)}</div>
                    
                    <div class="mb-3">
                        <h6 class="fw-bold">Tags:</h6>
                        ${tagsHtml || '<span class="text-muted">Nenhuma tag</span>'}
                    </div>
                </div>
            </div>
        `;
    } catch (err) {
        postContainer.innerHTML = `
            <div class="alert alert-danger mt-3 text-center">
                <h3>Oops! Erro ao carregar post.</h3>
                <p>${err.message}</p>
                <button class="btn btn-outline-danger mt-2" onclick="voltarParaLista()">Voltar</button>
            </div>
        `;
    }
}

function voltarParaLista() {
    // 1. Esconde a área do post completo e limpa ela
    document.getElementById('postCompleto').style.display = 'none';
    document.getElementById('postCompleto').innerHTML = '';

    // 2. Mostra a lista, a paginação e a busca novamente
    document.getElementById('areaBusca').style.display = 'flex'; // d-flex do bootstrap
    document.getElementById('listaPosts').style.display = 'block';
    document.getElementById('paginacao').style.display = 'flex'; // d-flex do bootstrap
}
  
// ==========================================
// FUNÇÕES AUXILIARES E EVENTOS INICIAIS
// ==========================================

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