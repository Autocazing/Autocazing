from aiokafka import AIOKafkaProducer, AIOKafkaConsumer
from json import dumps, loads
from core.config import get_setting

settings = get_setting()    # config.py에 설정해놓은 setting을 가져옴

# Kafka 프로듀서 인스턴스 생성
producer = AIOKafkaProducer(bootstrap_servers=[settings.KAFKA_BOOTSTRAP_SERVER0, settings.KAFKA_BOOTSTRAP_SERVER1, settings.KAFKA_BOOTSTRAP_SERVER2],
                        value_serializer=lambda x:dumps(x).encode('utf-8')  # 메시지의 값 직렬화
)

# Kafka 컨슈머 인스턴스 생성
def create_consumer(topic):
    return AIOKafkaConsumer(
        topic,
        bootstrap_servers=[settings.KAFKA_BOOTSTRAP_SERVER0, settings.KAFKA_BOOTSTRAP_SERVER1, settings.KAFKA_BOOTSTRAP_SERVER2],
        auto_offset_reset="earliest",  # 오프셋 위치(메시지를 읽어오는 시점)
        enable_auto_commit=True,  # 오프셋 자동 커밋 설정
        group_id='solution_server',  # solution 서버의 고유 소비자 그룹 ID
        value_deserializer=lambda x: loads(x.decode('utf-8'))  # 메시지의 값 역직렬화
    )
