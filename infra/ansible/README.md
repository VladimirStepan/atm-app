# Ansible (Docker install + backend deploy)

## 1. Prepare files
1. Copy `inventory.ini.example` to `inventory.ini` and set VM IP.
2. Copy `group_vars/all.yml.example` to `group_vars/all.yml`.
3. Fill `registry_id`, image name/tag, DB vars, and `yc_oauth_token`.

## 2. Install Docker on VM
```bash
cd infra/ansible
ansible-playbook -i inventory.ini install-docker.yml
```

## 3. Deploy backend + postgres
```bash
ansible-playbook -i inventory.ini deploy-backend.yml
```

## 4. Check on VM
```bash
ssh ubuntu@<VM_PUBLIC_IP>
docker compose -f /opt/atm-app/docker-compose.yml --env-file /opt/atm-app/.env ps
```
