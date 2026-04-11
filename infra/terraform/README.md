# Terraform (Yandex Cloud VM)

## 1. Prepare variables
1. Copy `terraform.tfvars.example` to `terraform.tfvars`.
2. Fill `yc_token`, `cloud_id`, `folder_id`, and your public SSH key.

## 2. Run
```bash
cd infra/terraform
terraform init
terraform fmt
terraform validate
terraform plan
terraform apply
```

## 3. Useful outputs
```bash
terraform output vm_public_ip
terraform output vm_id
```

Use `vm_public_ip` in Ansible inventory.

## 4. Cleanup
```bash
terraform destroy
```
