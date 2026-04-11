output "vm_id" {
  description = "Created VM ID."
  value       = yandex_compute_instance.atm_vm.id
}

output "vm_public_ip" {
  description = "Public IPv4 for SSH and deployment."
  value       = yandex_compute_instance.atm_vm.network_interface[0].nat_ip_address
}

output "vm_private_ip" {
  description = "Private IPv4 of the VM."
  value       = yandex_compute_instance.atm_vm.network_interface[0].ip_address
}
