resource "azurerm_container_registry" "acr" {
  name                = "${var.name-az}Acr"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku                 = "Basic"
  admin_enabled       = true
}

resource "null_resource" "push_image_to_acr" {
  provisioner "local-exec" {
    command = <<-EOT
      docker login ${azurerm_container_registry.acr.login_server}:443 -u ${azurerm_container_registry.acr.admin_username} -p ${azurerm_container_registry.acr.admin_password} && docker build -t ${azurerm_container_registry.acr.login_server}/${var.image_name} ../../items-marketplace/ && docker push ${azurerm_container_registry.acr.login_server}/${var.image_name}
    EOT
  }
}
