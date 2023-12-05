Команда для генерации grpc кода:

python -m grpc_tools.protoc -I proto proto/service.proto --python_out=./gen/python --pyi_out=./gen/python --grpc_python_out=./gen/python