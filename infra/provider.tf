terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "3.56.0"
    }
  }
  backend "s3" {
    bucket = "pgr301-stwe001-terraform"
    key    = "stwe001-terraform.tfstate"
    region = "eu-west-1"
  }
}