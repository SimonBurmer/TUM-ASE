.PHONY: test run build

test:
	@echo "------------------------------------------------------"
	@echo "-- TEST ----------------------------------------------"
	@echo "------------------------------------------------------"
ifeq ($(OS),Windows_NT)
	cd backendParent && mvnw.cmd test
else
	cd ./backendParent/; ./mvnw test
endif

build:
	@echo "------------------------------------------------------"
	@echo "-- BUILD ---------------------------------------------"
	@echo "------------------------------------------------------"
ifeq ($(OS),Windows_NT)
	cd backendParent && mvnw.cmd package -DskipTests
else
	cd ./backendParent/; ./mvnw  package -DskipTests
endif
	docker build -t auth_service-docker  authService/
	docker build -t delivery_service-docker  deliveryService/
	docker build -t discovery_service-docker  discoveryService/
	docker build -t email_service-docker  emailService/
	docker build -t gateway_service-docker  gatewayService/
	docker build -t ui_service-docker  uiService/
	docker build -t authentication-db-docker authService/authenticationDB
	docker build -t delivery-db-docker deliveryService/deliveryDB

run:
	@echo "------------------------------------------------------"
	@echo "-- RUN   ---------------------------------------------"
	@echo "------------------------------------------------------"
	docker-compose --env-file .env.local down --remove-orphans
	docker-compose --env-file .env.local up --abort-on-container-exit