// js/navbar.js
document.addEventListener('DOMContentLoaded', async () => {
    // Busca o usuário logado usando a função do seu api.js
    const user = await usuarioLogado();
    
    // Monta o HTML do menu
    const navHtml = `
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4 shadow">
            <div class="container">
                <a class="navbar-brand" href="/">Meu Blog</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuTopo">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="menuTopo">
                    <ul class="navbar-nav ms-auto">
                        ${user ? `
                            <li class="nav-item"><span class="nav-link text-light">Olá, ${user.nome}</span></li>
                            <li class="nav-item"><a class="nav-link btn btn-primary text-white ms-2" href="/novo-post.html">Criar Post</a></li>
                            <li class="nav-item"><button class="btn btn-link nav-link ms-2" onclick="fazerLogout()">Sair</button></li>
                        ` : `
                            <li class="nav-item"><a class="nav-link" href="/login.html">Entrar / Cadastrar</a></li>
                        `}
                    </ul>
                </div>
            </div>
        </nav>
    `;
    
    // Injeta o menu na div vazia do seu index.html
    const container = document.getElementById('navbar-container');
    if (container) container.innerHTML = navHtml;
});

// Função global para fazer o logout
window.fazerLogout = async () => {
    try {
        await apiPost('/auth/logout', {});
        window.location.href = '/'; // Redireciona para a home
    } catch (e) {
        alert('Erro ao sair: ' + e.message);
    }
};