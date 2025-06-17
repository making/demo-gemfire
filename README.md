# Demo Gemfire

```
./mvnw spring-boot:test-run
```

Go to http://localhost:8080

## GemFire Cluster Setup with OrbStack

See also https://ik.am/entries/854

```bash
orb create rocky:8 gemfire-locator-1 -c cloud-init-gemfire.yaml
orbctl clone gemfire-locator-1 gemfire-locator-2
orbctl clone gemfire-locator-1 gemfire-server-1
orbctl clone gemfire-locator-1 gemfire-server-2
orbctl clone gemfire-locator-1 gemfire-server-3
orbctl start gemfire-locator-2
orbctl start gemfire-server-1
orbctl start gemfire-server-2
orbctl start gemfire-server-3
orb -m gemfire-locator-1 -u gemfire gfsh start locator --name=locator-1 --port=10334 --hostname-for-clients=gemfire-locator-1.orb.local --jmx-manager-hostname-for-clients=gemfire-locator-1.orb.local --locators="gemfire-locator-1.orb.local[10334],gemfire-locator-2.orb.local[10334]" --max-heap=4g --dir=/opt/gemfire --classpath=/Users/toshiaki/git/demo-gemfire/target/classes
orb -m gemfire-locator-2 -u gemfire gfsh start locator --name=locator-2 --port=10334 --hostname-for-clients=gemfire-locator-2.orb.local --jmx-manager-hostname-for-clients=gemfire-locator-1.orb.local --locators="gemfire-locator-1.orb.local[10334],gemfire-locator-2.orb.local[10334]" --max-heap=4g --dir=/opt/gemfire --classpath=/Users/toshiaki/git/demo-gemfire/target/classes
orb -m gemfire-server-1 -u gemfire gfsh start server --name=server-1 --locators="gemfire-locator-1.orb.local[10334],gemfire-locator-2.orb.local[10334]" --hostname-for-clients=gemfire-server-1.orb.local --jmx-manager-hostname-for-clients=gemfire-server-1.orb.local --max-heap=4g --dir=/opt/gemfire --classpath=/Users/toshiaki/git/demo-gemfire/target/classes --cache-xml-file=/Users/toshiaki/git/demo-gemfire/src/test/resources/test-cache.xml
orb -m gemfire-server-2 -u gemfire gfsh start server --name=server-2 --locators="gemfire-locator-1.orb.local[10334],gemfire-locator-2.orb.local[10334]" --hostname-for-clients=gemfire-server-2.orb.local --jmx-manager-hostname-for-clients=gemfire-server-2.orb.local --max-heap=4g --dir=/opt/gemfire --classpath=/Users/toshiaki/git/demo-gemfire/target/classes --cache-xml-file=/Users/toshiaki/git/demo-gemfire/src/test/resources/test-cache.xml
orb -m gemfire-server-3 -u gemfire gfsh start server --name=server-3 --locators="gemfire-locator-1.orb.local[10334],gemfire-locator-2.orb.local[10334]" --hostname-for-clients=gemfire-server-3.orb.local --jmx-manager-hostname-for-clients=gemfire-server-3.orb.local --max-heap=4g --dir=/opt/gemfire --classpath=/Users/toshiaki/git/demo-gemfire/target/classes --cache-xml-file=/Users/toshiaki/git/demo-gemfire/src/test/resources/test-cache.xml
gfsh
```