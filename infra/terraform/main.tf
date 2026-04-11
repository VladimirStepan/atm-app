provider "yandex" {
  token     = var.yc_token
  cloud_id  = var.cloud_id
  folder_id = var.folder_id
  zone      = var.zone
}

data "yandex_compute_image" "ubuntu" {
  family    = var.image_family
  folder_id = "standard-images"
}

resource "yandex_vpc_network" "atm" {
  name = var.network_name
}

resource "yandex_vpc_subnet" "atm" {
  name           = var.subnet_name
  zone           = var.zone
  network_id     = yandex_vpc_network.atm.id
  v4_cidr_blocks = [var.subnet_cidr]
}

resource "yandex_compute_instance" "atm_vm" {
  name        = var.vm_name
  platform_id = "standard-v3"

  resources {
    cores         = var.vm_cores
    memory        = var.vm_memory
    core_fraction = var.core_fraction
  }

  boot_disk {
    initialize_params {
      image_id = data.yandex_compute_image.ubuntu.id
      size     = var.disk_size_gb
      type     = "network-hdd"
    }
  }

  network_interface {
    subnet_id = yandex_vpc_subnet.atm.id
    nat       = true
  }

  metadata = {
    ssh-keys = "${var.ssh_user}:${var.ssh_public_key}"
  }
}
