---

Tenho um mini datacenter em laboratório com duas VMs principais:
- vm-app: servidor de aplicação, IP externo 192.168.0.39 (vmbr0) e IP interno 192.168.100.10 (vmbr1). Já possui Docker e Nginx.
- vm-db: servidor de banco de dados, IP interno 192.168.100.20 (vmbr1). Já possui Docker e PostgreSQL rodando em container.

A rede vmbr0 é externa (PC → vm-app) e a rede vmbr1 é interna (vm-app ↔ vm-db).
Quero instalar e configurar n8n nesse ambiente.

## 📝 Prompt para novo chat dedicado ao n8n

Você pode abrir um novo chat e colar este prompt:

Quero instalar o n8n na vm-app do meu mini datacenter.

    Sistema: Ubuntu 24.04 LTS (Noble).

    Infraestrutura: Proxmox VE 9.2.2 (pve-lab).

    vm-app: IP externo 192.168.0.39, interno 192.168.100.10.

    vm-db: PostgreSQL rodando em 192.168.100.20.

    PC: 192.168.0.34 acessa a vm-app via navegador.

Preciso de um passo a passo completo para rodar o n8n em container Docker na vm-app, usando o PostgreSQL da vm-db como banco de dados, com boas práticas de segurança, persistência e backup.




