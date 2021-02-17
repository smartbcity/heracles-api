#!/usr/bin/env bash
cat /opt/commune-sandbox/util/env
source /opt/commune-sandbox/util/env

#Join network
echo '/////////////////////////////////////////////////'
echo '/////////////////////////////////////////////////'
echo '/////////////////////////////////////////////////'
echo '/////////////////////////////////////////////////'
echo 'Join channel' ${CHANNEL}
peer channel create -o ${ORDERER_ADDR} -c ${CHANNEL} -f /etc/hyperledger/config/${CHANNEL}.tx --tls --cafile ${ORDERER_CERT}

ls -al

peer channel join -b ${CHANNEL}.block

#Prepare ex02
cd /opt/gopath/src/chaincode_example02
go mod init
go get github.com/golang/protobuf/proto
go get google.golang.org/grpc
go get github.com/hyperledger/fabric-protos-go
go mod vendor

#Install and instantiate ex02
echo 'Install chaincode ex02'
peer chaincode install -n ex02 -v 1.0 -p chaincode_example02/
echo 'Instantiate chaincode ex02'
peer chaincode instantiate -o ${ORDERER_ADDR} --tls --cafile ${ORDERER_CERT} -C ${CHANNEL} -n ex02 -v 1.0 -c '{"Args":["init","a", "100", "b","200"]}' -P "OR ('BlockchainLANCoopMSP.member')"
sleep 5
echo 'Query chaincode ex02'
peer chaincode query -C ${CHANNEL} -n ex02 -c '{"Args":["query","a"]}'
