from kafka import KafkaProducer, KafkaConsumer
from json import dumps, loads
from core.config import get_setting

settings = get_setting()    # config.py에 설정해놓은 setting을 가져옴

# Kafka 프로듀서 인스턴스 생성
producer = KafkaProducer(bootstrap_servers=settings.KAFKA_BOOTSTRAP_SERVERS,
                        value_serializer=lambda x:dumps(x).encode('utf-8')  # 메시지의 값 직렬화
)

# Kafka 컨슈머 인스턴스 생성
consumer = KafkaConsumer('test',
                        bootstrap_servers=settings.KAFKA_BOOTSTRAP_SERVERS,
                        auto_offset_reset="earliest",  # 오프셋 위치(메시지를 읽어오는 시점, earliest: 가장 처음(구독 전부터), latest: 가장 최근(구독 후부터))
                        enable_auto_commit=True,  # 오프셋 자동 커밋 설정
                        group_id="solutionGroup",
                        value_deserializer=lambda x: loads(x.decode('utf-8'))  # 메시지의 값 역직렬화
)
