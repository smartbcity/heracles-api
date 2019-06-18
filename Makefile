SDK_REST_JAVA_NAME	    := civisblockchain/coop-rest-java
SDK_REST_JAVA_IMG	    := ${SDK_REST_JAVA_NAME}:${VERSION}
SDK_REST_JAVA_LATEST	:= ${SDK_REST_JAVA_NAME}:latest

clean:
	@./gradlew clean

test:
	@./gradlew test

package:
	@docker build -f Dockerfile -t ${SDK_REST_JAVA_IMG} .

push:
	@docker push ${SDK_REST_JAVA_IMG}

push-latest:
	@docker tag ${SDK_REST_JAVA_IMG} ${SDK_REST_JAVA_LATEST}
	@docker push ${SDK_REST_JAVA_LATEST}
