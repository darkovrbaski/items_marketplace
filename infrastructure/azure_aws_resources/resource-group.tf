resource "azurerm_resource_group" "rg" {
  name     = "${var.name-az}Rg"
  location = var.azure_location
}

data "azurerm_client_config" "current" {}
