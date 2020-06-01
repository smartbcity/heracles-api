clean:
	@gradle clean

test:
	@gradle test -i

package:
	@gradle jibDockerBuild

push:
	@gradle jib

push-latest:
	VERSION=latest gradle jib
