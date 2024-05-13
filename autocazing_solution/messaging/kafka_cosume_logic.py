import asyncio
import json
from messaging.kafka_instance import consumer

# 비동기 Kafka 메시지 소비
async def consume_messages():
    loop = asyncio.get_running_loop()
    while True:
        message = await loop.run_in_executor(None, consumer.poll, 1.0)
        if not message:
            continue
            
        for message in consumer:
            print(f'Received message: {message.value}')
            # 필요한 메시지 처리 로직 추가
            process_message(message.value)

def process_message(data):
    # 메시지 처리 로직
    print("Processing:", data)