import socket
import cv2
import numpy as np
import time
import threading
from analysisVideo import AnalyzeVideo

def create_thread(s_socket) :
    global index
    t.append(Cserver(s_socket))
    t[index].deamon = True
    t[index].start()

#socket에서 수신한 버퍼를 반환하는 함수
def recvall(sock, count):
    # 바이트 문자열
    buf = b''
    while count:
        newbuf = sock.recv(count)
        if not newbuf: return None
        buf += newbuf
        count -= len(newbuf)
    return buf

def handle_client(client, addr, index, analyze) :
    q = 0
    print("Connect with ", addr)
    while True :
        length = recvall(client, 12)
        print("Receive :", length)
        stringData = recvall(client, int(length))
        data = np.fromstring(stringData, dtype = 'uint8')

        width = 640
        height = 360

        b = np.zeros([height,width,1])

        t = 0
        for i in range(height) :
            for k in range(width) :
                # for d in range(3) :
                b[i, k, 0] = data[t]
                t += 1
        print("Done", q)
        name = str(index)+'photo'+str(q)+'.jpg'

        cv2.imwrite(name, b)
        t = threading.Thread(target = analyze._analyzeFace, args = (name,q, ))
        t.daemon = True
        t.start()

        q += 1
    client.close()

HOST='192.168.0.56'
PORT=8000
analyze = AnalyzeVideo()

#TCP 사용
s = socket.socket(socket.AF_INET)
print('Socket created')
 
#서버의 아이피와 포트번호 지정
s.bind((HOST,PORT))
print('Socket bind complete')
# 클라이언트의 접속을 기다린다. (클라이언트 연결을 10개까지 받는다)
s.listen(10)
print('Socket now listening')

index = 0

while True :
    try :
        client, addr = s.accept()
    except KeyboardInterrupt :
        s.close()
    
    t = threading.Thread(target = handle_client, args = (client, addr, index, analyze))
    index += 1
    t.daemon = True
    t.start()