Tenho um mini datacenter em laboratório com duas VMs principais:
- vm-app: servidor de aplicação, IP externo 192.168.0.39 (vmbr0) e IP interno 192.168.100.10 (vmbr1). Já possui Docker e Nginx.
- vm-db: servidor de banco de dados, IP interno 192.168.100.20 (vmbr1). Já possui Docker e PostgreSQL rodando em container.

A rede vmbr0 é externa (PC → vm-app) e a rede vmbr1 é interna (vm-app ↔ vm-db).
Quero instalar e configurar [NOME DA APLICAÇÃO] nesse ambiente.
