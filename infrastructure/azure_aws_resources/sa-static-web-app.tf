# resource "azurerm_storage_account" "sa" {
#   name                      = "itemsmarketplacesa"
#   resource_group_name       = azurerm_resource_group.rg.name
#   location                  = azurerm_resource_group.rg.location
#   account_tier              = "Standard"
#   account_replication_type  = "LRS"
#   account_kind              = "StorageV2"
#   enable_https_traffic_only = true

#   static_website {
#     index_document = "index.html"
#   }
# }
