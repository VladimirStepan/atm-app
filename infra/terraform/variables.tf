variable "yc_token" {
  description = "Yandex Cloud OAuth token or IAM token."
  type        = string
  sensitive   = true
}

variable "cloud_id" {
  description = "Yandex Cloud cloud_id."
  type        = string
}

variable "folder_id" {
  description = "Yandex Cloud folder_id."
  type        = string
}

variable "zone" {
  description = "Availability zone for VM and subnet."
  type        = string
  default     = "ru-central1-a"
}

variable "network_name" {
  description = "VPC network name."
  type        = string
  default     = "atm-network"
}

variable "subnet_name" {
  description = "Subnet name."
  type        = string
  default     = "atm-subnet"
}

variable "subnet_cidr" {
  description = "CIDR for subnet."
  type        = string
  default     = "10.10.0.0/24"
}

variable "vm_name" {
  description = "Compute instance name."
  type        = string
  default     = "atm-vm"
}

variable "vm_cores" {
  description = "Number of VM vCPUs."
  type        = number
  default     = 2
}

variable "vm_memory" {
  description = "VM RAM in GB."
  type        = number
  default     = 2
}

variable "core_fraction" {
  description = "Guaranteed CPU fraction."
  type        = number
  default     = 20
}

variable "disk_size_gb" {
  description = "Boot disk size in GB."
  type        = number
  default     = 20
}

variable "image_family" {
  description = "Image family from standard-images."
  type        = string
  default     = "ubuntu-2204-lts"
}

variable "ssh_user" {
  description = "Linux user for SSH key metadata."
  type        = string
  default     = "ubuntu"
}

variable "ssh_public_key" {
  description = "Public SSH key content, for example: ssh-ed25519 AAAA... user@host."
  type        = string
}
