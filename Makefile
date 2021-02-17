clean:
	@gradle clean

test:
	@gradle test -i

package:
	./gradlew jibDockerBuild

push:
	./gradlew jib

push-latest:
	VERSION=latest gradle jib
