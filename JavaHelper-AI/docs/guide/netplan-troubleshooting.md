
```markdown
# Troubleshooting Netplan — Ubuntu Server

## 🔍 Verificar sintaxe YAML
- Use espaços, nunca TAB.
- Confirme indentação correta.
- Teste antes de aplicar:
```bash
sudo netplan try
```

## 🔧 Checar interface correta
- Liste interfaces:
```bash
ip a
```
- Confirme se o nome usado (`ens18`, `eth0`, etc.) corresponde ao arquivo YAML.

## 📡 Validar aplicação
- Aplicar configuração:
```bash
sudo netplan apply
```
- Verificar status:
```bash
netplan status
```

## 🌐 Testar conectividade
- Ping gateway:
```bash
ping 192.168.0.1
```
- Ping DNS:
```bash
ping 8.8.8.8
```
- Testar resolução:
```bash
ping google.com
```

## 🛡️ Checar rotas
```bash
ip route
```
- Deve existir uma rota `default via 192.168.0.1`.

## 📖 Logs detalhados
```bash
journalctl -u systemd-networkd --no-pager
```
- Mostra erros de DHCP, rotas e aplicação de configuração.

---

## ✅ Resultado esperado
- Identificação rápida de erros de **sintaxe**, **interface incorreta**, **rota ausente** ou **DNS quebrado**.
- Netplan aplicado corretamente e VM com conectividade estável.
```

---
```markdown
# Checklist Netplan — Configuração de IP Fixo

## 🔍 Identificar interface de rede
```bash
ip a
```
- Verifique o nome da interface (ex: `ens18`, `eth0`).

---

## 📝 Editar arquivo Netplan
```bash
sudo nano /etc/netplan/50-cloud-init.yaml
```

### Exemplo de configuração:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens18:
      dhcp4: false
      addresses:
        - 192.168.0.25/24
      gateway4: 192.168.0.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1
```

---

## ⚙️ Aplicar configuração
```bash
sudo netplan apply
```

---

## 🌐 Testar conectividade
- Ping gateway:
```bash
ping 192.168.0.1
```
- Ping DNS:
```bash
ping 8.8.8.8
```
- Testar resolução:
```bash
ping google.com
```

---

## 🛡️ Validar rotas
```bash
ip route
```
- Deve existir uma rota `default via 192.168.0.1`.

---

## 📖 Logs para diagnóstico
```bash
journalctl -u systemd-networkd --no-pager
```
- Verifique erros de DHCP, rotas ou aplicação de configuração.

---

## ✅ Resultado esperado
- Interface configurada com IP fixo.
- Conectividade estável com gateway e DNS.
- Rotas corretas e resolução de nomes funcionando.
```

---


```markdown
# Checklist Netplan — Configuração de Múltiplas Rotas

## 🔍 Identificar interface de rede
```bash
ip a
```
- Verifique o nome da interface (ex: `ens18`, `eth0`).

---

## 📝 Editar arquivo Netplan
```bash
sudo nano /etc/netplan/50-cloud-init.yaml
```

### Exemplo de configuração com múltiplas rotas:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens18:
      dhcp4: false
      addresses:
        - 192.168.0.25/24
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1
      routes:
        - to: default
          via: 192.168.0.1
        - to: 192.168.10.0/24
          via: 192.168.0.254
        - to: 10.0.0.0/16
          via: 192.168.0.100
```

---

## ⚙️ Aplicar configuração
```bash
sudo netplan apply
```

---

## 🌐 Testar conectividade
- Ping gateway principal:
```bash
ping 192.168.0.1
```
- Testar rota interna:
```bash
ping 192.168.10.1
```
- Testar rota remota:
```bash
ping 10.0.0.1
```

---

## 🛡️ Validar rotas
```bash
ip route
```
- Deve listar todas as rotas configuradas (`default`, `192.168.10.0/24`, `10.0.0.0/16`).

---

## 📖 Logs para diagnóstico
```bash
journalctl -u systemd-networkd --no-pager
```
- Verifique erros de aplicação de rotas.

---

## ✅ Resultado esperado
- Interface com IP fixo.
- Gateway padrão configurado.
- Rotas adicionais para redes internas ou VPN.
- Conectividade estável e múltiplos caminhos de rede funcionando.
```


---

```markdown
# Checklist Netplan — Configuração de Múltiplos NICs

## 🔍 Identificar interfaces de rede
```bash
ip a
```
- Verifique os nomes das interfaces (ex: `ens18`, `ens19`, `eth0`, `eth1`).

---

## 📝 Editar arquivo Netplan
```bash
sudo nano /etc/netplan/50-cloud-init.yaml
```

### Exemplo de configuração com duas interfaces:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens18:
      dhcp4: false
      addresses:
        - 192.168.0.25/24
      gateway4: 192.168.0.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1

    ens19:
      dhcp4: false
      addresses:
        - 10.0.0.25/24
      routes:
        - to: 10.0.0.0/16
          via: 10.0.0.1
```

---

## ⚙️ Aplicar configuração
```bash
sudo netplan apply
```

---

## 🌐 Testar conectividade
- Ping gateway da rede principal:
```bash
ping 192.168.0.1
```
- Ping gateway da rede secundária:
```bash
ping 10.0.0.1
```

---

## 🛡️ Validar rotas
```bash
ip route
```
- Deve listar `default via 192.168.0.1` e rota para `10.0.0.0/16 via 10.0.0.1`.

---

## 📖 Logs para diagnóstico
```bash
journalctl -u systemd-networkd --no-pager
```
- Verifique erros de aplicação de múltiplas interfaces.

---

## ✅ Resultado esperado
- Cada NIC configurada com IP fixo.
- Gateway padrão na rede principal.
- Rotas adicionais para redes secundárias.
- Conectividade estável em múltiplas redes.
```

---


```markdown
# Checklist Netplan — Configuração de VLANs

## 🔍 Identificar interface de rede
```bash
ip a
```
- Verifique o nome da interface física (ex: `ens18`, `eth0`).

---

## 📝 Editar arquivo Netplan
```bash
sudo nano /etc/netplan/50-cloud-init.yaml
```

### Exemplo de configuração com VLANs:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens18:
      dhcp4: false

  vlans:
    vlan10:
      id: 10
      link: ens18
      addresses:
        - 192.168.10.25/24
      gateway4: 192.168.10.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1

    vlan20:
      id: 20
      link: ens18
      addresses:
        - 192.168.20.25/24
      gateway4: 192.168.20.1
      nameservers:
        addresses:
          - 1.1.1.1
          - 9.9.9.9
```

---

## ⚙️ Aplicar configuração
```bash
sudo netplan apply
```

---

## 🌐 Testar conectividade
- Ping gateway da VLAN 10:
```bash
ping 192.168.10.1
```
- Ping gateway da VLAN 20:
```bash
ping 192.168.20.1
```

---

## 🛡️ Validar VLANs
```bash
ip -d link show
```
- Deve listar `vlan10` e `vlan20` como interfaces virtuais ligadas a `ens18`.

---

## 📖 Logs para diagnóstico
```bash
journalctl -u systemd-networkd --no-pager
```
- Verifique erros de aplicação de VLANs.

---

## ✅ Resultado esperado
- Interface física (`ens18`) configurada como base.  
- VLANs criadas (`vlan10`, `vlan20`) com IPs e gateways distintos.  
- Conectividade estável e segmentação de tráfego funcionando.
```

---


```markdown
# Checklist Netplan — Configuração de Bridges

## 🔍 Identificar interfaces de rede
```bash
ip a
```
- Verifique os nomes das interfaces físicas (ex: `ens18`, `ens19`).

---

## 📝 Editar arquivo Netplan
```bash
sudo nano /etc/netplan/50-cloud-init.yaml
```

### Exemplo de configuração com bridge:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens18:
      dhcp4: false
    ens19:
      dhcp4: false

  bridges:
    br0:
      interfaces: [ens18, ens19]
      dhcp4: false
      addresses:
        - 192.168.0.25/24
      gateway4: 192.168.0.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1
```

---

## ⚙️ Aplicar configuração
```bash
sudo netplan apply
```

---

## 🌐 Testar conectividade
- Ping gateway:
```bash
ping 192.168.0.1
```
- Testar resolução DNS:
```bash
ping google.com
```

---

## 🛡️ Validar bridge
```bash
ip link show br0
```
- Deve listar a interface `br0` como ativa.
- `br0` será usada como interface principal, agregando `ens18` e `ens19`.

---

## 📖 Logs para diagnóstico
```bash
journalctl -u systemd-networkd --no-pager
```
- Verifique erros de aplicação da bridge.

---

## ✅ Resultado esperado
- Interfaces físicas (`ens18`, `ens19`) agregadas em uma bridge (`br0`).
- IP fixo configurado na bridge.
- Gateway e DNS funcionando.
- Conectividade estável para containers, VMs ou serviços ligados à bridge.
```

---


```markdown
# Checklist Netplan — Configuração de Bonds (Link Aggregation)

## 🔍 Identificar interfaces de rede
```bash
ip a
```
- Verifique os nomes das interfaces físicas (ex: `ens18`, `ens19`).

---

## 📝 Editar arquivo Netplan
```bash
sudo nano /etc/netplan/50-cloud-init.yaml
```

### Exemplo de configuração com bond:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens18: {}
    ens19: {}

  bonds:
    bond0:
      interfaces: [ens18, ens19]
      parameters:
        mode: balance-rr        # opções: balance-rr, active-backup, 802.3ad, etc.
        mii-monitor-interval: 100
      dhcp4: false
      addresses:
        - 192.168.0.25/24
      gateway4: 192.168.0.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1
```

---

## ⚙️ Aplicar configuração
```bash
sudo netplan apply
```

---

## 🌐 Testar conectividade
- Ping gateway:
```bash
ping 192.168.0.1
```
- Testar resolução DNS:
```bash
ping google.com
```

---

## 🛡️ Validar bond
```bash
cat /proc/net/bonding/bond0
```
- Deve listar `ens18` e `ens19` como escravos do bond.
- Mostra o modo de operação (`balance-rr`, `active-backup`, etc.).

---

## 📖 Logs para diagnóstico
```bash
journalctl -u systemd-networkd --no-pager
```
- Verifique erros de aplicação do bond.

---

## ✅ Resultado esperado
- Interfaces físicas (`ens18`, `ens19`) agregadas em um bond (`bond0`).
- IP fixo configurado no bond.
- Gateway e DNS funcionando.
- Redundância ou maior throughput de rede garantido.
```


---

```markdown
# Checklist Netplan — Configuração de Tunnels (GRE/IPsec)

## 🔍 Identificar interface de rede
```bash
ip a
```
- Verifique o nome da interface física (ex: `ens18`, `eth0`).

---

## 📝 Editar arquivo Netplan
```bash
sudo nano /etc/netplan/50-cloud-init.yaml
```

### Exemplo de configuração com túnel GRE:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens18:
      dhcp4: false
      addresses:
        - 192.168.0.25/24
      gateway4: 192.168.0.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 1.1.1.1

  tunnels:
    gre1:
      mode: gre
      local: 192.168.0.25
      remote: 203.0.113.10
      addresses:
        - 10.10.10.1/24
```

---

## ⚙️ Aplicar configuração
```bash
sudo netplan apply
```

---

## 🌐 Testar conectividade
- Ping IP remoto do túnel:
```bash
ping 203.0.113.10
```
- Ping IP interno do túnel:
```bash
ping 10.10.10.2
```

---

## 🛡️ Validar túnel
```bash
ip tunnel show
```
- Deve listar `gre1` como túnel ativo.

---

## 📖 Logs para diagnóstico
```bash
journalctl -u systemd-networkd --no-pager
```
- Verifique erros de aplicação do túnel.

---

## ✅ Resultado esperado
- Interface física configurada com IP fixo.  
- Túnel GRE criado (`gre1`) entre os hosts.  
- IP interno do túnel funcionando para comunicação segura.  
- Conectividade estável entre redes remotas.
```

---



