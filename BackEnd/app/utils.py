import os

file_path = "/home/testing/Navchetna/app/SPYM_1.jpg"

if os.access(file_path, os.W_OK):
    print("Write permissions are granted for the file.")
else:
    print("Write permissions are not granted for the file.")

if os.access(file_path, os.X_OK):
    print("Execute permissions are granted for the file.")
else:
    print("Execute permissions are not granted for the file.")