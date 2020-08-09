# -*- coding: UTF-8 -*-
import pygame as pygame
import random
import time
from pygame_functions import *

FRAME_UPDATE_INTERVAL  = 100
FONT_FAMILY = "./font/neodgm.ttf"
LOSE_CODE = 1

class Player:
    def __init__(self, imagePath, xPos, yPos):
        self.MOVE_DISTANCE = 3   # 좌우 키를 눌렀을 때, 캐릭터가 움직이는 x좌표의 길이
        self.HEALTH_REDUCTION_WIDTH = 2   # 초당 감소하는 플레이어의 체력
        self.MAX_HEALTH = 106
        self.BIG_IMAGE_DURATION = 10    # 몸집이 커지는 효과의 지속시간

        self.isBig = False # 플레이어가 현재 충돌 효과로 몸집이 커졌는지를 나타내는 flag
        self.xPos = xPos    # 플레이어의 x좌표
        self.yPos = yPos    # 플레이어의 y좌표
        self.health = self.MAX_HEALTH   # 플레이어의 체력
        self.gameResult = 0 # 게임 결과를 나타내며, 결과에 따라 0 ~ 4의 값을 가짐
        self.gameOver = False   # 게임이 종료되었는지를 나타내는 flag (while문을 벗어나는데 사용된다)
        self.healthBarList = [] # 1 pixel의 healthbar 이미지들을 담는 list
        self.healthBar = None
        self.bigImageStartTime = None   # 몸집이 커지는 효과가 적용된 시간
        self.touchingOpponentList = []
        
        self.character = makeSprite(imagePath)
        addSpriteImage(self.character, "./images\character/bigQuidditchPotter.png")
        moveSprite(self.character, xPos, yPos)
        showSprite(self.character)

    def playerMove(self, direction = None):    # 플레이어가 입력한 방향키에 따라 캐릭터를 움직임
        for i in range(0, self.MOVE_DISTANCE):    # 캐릭터를 distance만큼 1씩 이동시킴
            changedXpos = self.xPos + 1 if direction == "right" else self.xPos - 1

            if(changedXpos >= 0 and changedXpos <= 640):    # 움직였을 때, 맵을 벗어나지 않는 경우에만 캐릭터를 움직임
                self.xPos = changedXpos
                moveSprite(self.character, self.xPos, self.yPos, False, False)

        # 플레이어가 이동함에 따라 체력을 같이 움직임
        plusXpos = 15 if self.isBig == True else 25

        for index, health in enumerate(self.healthBarList):
            moveSprite(health, self.xPos - plusXpos + index, self.yPos -10)

        # 플레이어가 이동함에 따라 체력바를 같이 움직임
        moveSprite(self.healthBar, self.xPos - plusXpos - 2, self.yPos -12)

    def timeToSmall(self):
        curTime = pygame.time.get_ticks()   # 현재 시각

        if self.bigImageStartTime + self.BIG_IMAGE_DURATION * 1000 < curTime :    # 몸집이 커지는 시간이 지난 경우
            changeSpriteImage(self.character, 0)
            self.isBig = False
            self.playerMove()


class Monster:
    def __init__(self, monsterList, imagePath, xPos, yPos, advance, directionFrame, totalFrame = 0):
        self.TOTAL_DISTANCE = 500  # 몬스터가 이동하는 구간의 길이
        self.MONSTER_DIRECTION_FRAME = directionFrame  # 몬스터를 표현하는 프레임 개수
        self.MOVE_DISTANCE = 3 # 몬스터가 반복문에서 한 번의 반복마다 움직이는 거리
        self.EXTINCTION_FRAME = 7
        self.FRAME_UPDATE_INTERVAL = 100

        self.xPos = xPos    # 몬스터의 첫 x좌표
        self.yPos = yPos    # 몬스터의 첫 y좌표
        self.advance  = advance    # advance가 True라면, 오른쪽으로 움직일 차례임을 나타냄
        self.move = 0   # 현재 진행 방향으로 움직인 횟수(거리)를 나타냄
        self.nextFrame = clock()  # 프레임을 변경할 시간을 나타냄
        self.frame = 0  # 프레임 번호
        self.healthBarList = [] # 1 pixel의 healthbar 이미지들을 담는 list
        self.health = 106   # 몬스터의 체력
        self.touchingMagic = None # 가장 최근에 이미지가 충돌한 마법 (몬스터의 이미지가 마법의 이미지와 충돌하는 동안 HP가 지속적으로 감소하여 너무 많은 HP가 감소되어 flag를 두어 한개의 마법에 한번만 체력을 소모하도록 하기 위해 필요함)
        self.healthBar = None   # 몬스터가 움직이면서 체력바도 같이 움직여야하기 때문에 필요함
        self.curFrame = 0

        if self.advance == True:
            self.extinctionImage = makeSprite("./images/monster/dementorExtinctionRight.gif", 7)
        elif self.advance == False:
            self.extinctionImage = makeSprite("./images/monster/dementorExtinctionLeft.gif", 7)

        self.image = makeSprite(imagePath, totalFrame)
        moveSprite(self.image, self.xPos, self.yPos, True)
        showSprite(self.image)
        monsterList.append(self)    #몬스터 list에 추가

    def monsterMove(self):
        if clock() > self.nextFrame:  # FRAME_UPDATE_INTERVAL마다 몬스터의 frame을 update함
            self.frame = (self.frame + 1) % self.MONSTER_DIRECTION_FRAME  # 몬스터의 frame 번호를 갱신 (modulus를 사용하여 frame 번호가 0 ~ self.MONSTER_TOTAL_FRAME -1을 반복하도록 함)
            self.nextFrame += FRAME_UPDATE_INTERVAL  # 다음 프레임이 나타나는 시간(nextFrame)이 되면 frame 번호를 갱신 -> frame을 갱신하지 않으면 몬스터의 동작이 바뀌지 않고, nextFrame을 갱신하지 않으면 동작이 너무 빨리 바뀜.

            # 캐릭터가 움직이는 방향에 맞춰 이미지를 바꿔줌
            if self.advance == True:
                changeSpriteImage(self.image, 1 * self.MONSTER_DIRECTION_FRAME + self.frame)
            elif self.advance == False:
                changeSpriteImage(self.image, 0 * self.MONSTER_DIRECTION_FRAME + self.frame)

        cur_movement = self.MOVE_DISTANCE if self.advance else -self.MOVE_DISTANCE    # 오른쪽으로 움직이면 몬스터의 현재 위치에 plus를, 왼쪽으로 움직이면 minus를 더해줌
        self.xPos += cur_movement

        moveSprite(self.image, self.xPos, self.yPos, True, False)  # 몬스터를 이동시킴
        self.move += self.MOVE_DISTANCE  # 몬스터가 진행 방향으로 움직인 거리를 1 증가시킴

        # 몬스터가 이동함에 따라 체력을 같이 움직임
        for index, health in enumerate(self.healthBarList):
            moveSprite(health, self.xPos - 53 + index, self.yPos + 62)

        # 몬스터가 이동함에 따라 체력바를 같이 움직임
        moveSprite(self.healthBar, self.xPos - 55, self.yPos + 60)

        if self.move > self.TOTAL_DISTANCE:  # 왔다 갔다 할 수 있도록 진행 방향을 바꿔줌
            self.move = 0  # 해당 방향으로 움직인 거리는 0으로 값을 바꿈
            self.advance = not self.advance  # 진행 방향 바꿈

    def extinctionMotion(self, monsterList, labelList):
        showSprite(self.extinctionImage)
        moveSprite(self.extinctionImage, self.xPos, self.yPos, True)
        hideSprite(self.healthBar)  # 몬스터의 체력바 이미지를 숨김

        if clock() > self.nextFrame:
            self.curFrame = self.curFrame + 1
            self.nextFrame += FRAME_UPDATE_INTERVAL  # 다음 프레임이 나타나는 시간(nextFrame)이 되면 frame 번호를 갱신 -> frame을 갱신하지 않으면 동작이 바뀌지 않고, nextFrame을 갱신하지 않으면 동작이 너무 빨리 바뀜. # so the modulus 8 allows it to loop
            changeSpriteImage(self.extinctionImage, self.curFrame)

            if(self.curFrame == self.EXTINCTION_FRAME - 1):
                hideSprite(self.extinctionImage)
                monsterList.remove(self)
                if len(monsterList) == 0:  # 몬스터를 모두 처치한 경우
                    Label(labelList, 1003, 1.5, "디멘터 처치 완료!", 25, 250, 300, "white", FONT_FAMILY)

    def statusInit(self):
        # 체력바
        self.healthBar = makeSprite("./images/status/smallHealthbar.png")
        moveSprite(self.healthBar, 460, 80)
        showSprite(self.healthBar)

        # 체력바 위에 체력을 그림
        drawHealth(self.healthBarList, "./images/status/smallHealth.png", 0, self.health, self.xPos - 55, self.yPos + 60, 2)

class Opponent:
    def __init__(self, opponentList, startPos):
        self.MOVE_DISTANCE = 3  # 상대 팀원이 반복문에서 한 번의 반복마다 움직이는 거리
        self.POWER = 8 # 플레이어와 충돌하였을 때, 플레이어가 입는 피해

        self.startPos = startPos    # startPos는 상대 팀원이 오른쪽에서 나타나는지 왼쪽에서 나타나는지를 나타냄(0이면 왼쪽, 1이면 오른쪽)
        self.destinationXPos = random.randint(0, 350) if(startPos == 0) else random.randint(300, 650)   # destinationXPos는 상대 팀원이 방향을 바꾸는 x좌표를 나타냄
        self.xPos = 0 if(startPos == 0) else 700 # 상대 팀원의 시작점 x좌표 (상대 팀원이 나타나는 지점에 따라 시작점이 다름)
        self.yPos = 100 # 상대팀원의 시작점 y좌표
        self.is_changed = False # 이미지가 바뀌었는지를 나타내는 flag, 방향을 바꾼 뒤 이미지를 한번만 바꾸기 위해 필요함

        # 상대 팀원이 나타나는 지점에 따라 초기 이미지를 결정
        if startPos == 1 :
            self.opponent = makeSprite("./images/character/opponent1.png")
        elif startPos == 0:
            self.opponent = makeSprite("./images/character/opponent2.png")

        opponentList.append(self)
        moveSprite(self.opponent, self.xPos, self.yPos)
        showSprite(self.opponent)

        # 방향을 바꾼 뒤의 이미지를 추가
        addSpriteImage(self.opponent, "./images/character/opponent3.png")

    def opponentMove(self, opponentList):
        if self.startPos == 1 and self.xPos > self.destinationXPos :    # 오른쪽에서 시작하는 상대팀이 방향을 바꾸는 지점에 도착하지 못한 경우 x좌표만 이동
            self.xPos -= self.MOVE_DISTANCE
        elif self.startPos == 0 and self.xPos < self.destinationXPos :  # 왼쪽에서 시작하는 상대팀이 방향을 바꾸는 지점에 도착하지 못한 경우 x좌표만 이동
            self.xPos += self.MOVE_DISTANCE
        elif self.yPos < 700 :  # 방향을 바꾸는 지점에 도착한 이후, 맵에서 사라지기 전인 경우
            if self.is_changed == False :   # 방향을 바꿧는지를 나타내는 flag를 확인 후, 방향을 바꾸지 않았다면 방향을 바꾸고 flag를 update
                changeSpriteImage(self.opponent, 1)
                self.is_changed = True
            self.yPos += self.MOVE_DISTANCE  # 맵 밖으로 나갈 때까지 y좌표에 값을 더해줌
        else :
            hideSprite(self.opponent)   # 맵 밖으로 나간 경우 이미지를 숨김
            opponentList.remove(self)   # 리스트에서 제거

        moveSprite(self.opponent, self.xPos, self.yPos, False, False)   # 바뀐 좌표로 상대 팀원을 이동 시킴.

class Magic:
    def __init__(self, xPos, yPos):
        self.magicName = ["Avada Kedavra", "Aresto Momentum", "Arania exuma", "Aparecium"]
        self.randomNum = random.randint(0, 3)
        self.DURATION = 0.3 # 마법 이미지가 화면에 표시 되는 시간
        self.castTime = pygame.time.get_ticks() # 마법이 시전된 시간
        self.POWER = 3  # 마법 한대를 맞았을 때 감소하는 hp의 양

        self.magicLabel = makeLabel(self.magicName[self.randomNum] + "!", 30, xPos + 50, 480, "white", FONT_FAMILY, is_italic = True)    # 마법 이름을 이탤릭체로 마법 이미지 옆에 나타냄
        showLabel(self.magicLabel)

        imagePath = "./images/magic/magic" + str(self.randomNum + 1) + ".png"
        self.image = makeSprite(imagePath)
        moveSprite(self.image, xPos, yPos)
        showSprite(self.image)

    def is_timeout(self):   # 마법의 시전 시간이 지났는지 나타내는 boolean 타입을 반환하는 함수
        if self.castTime + self.DURATION * 1000 < pygame.time.get_ticks(): # 시전 시간이 지난 경우
            return True
        else :
            return False

class Item:
    def __init__(self, itemList, itemName, advance):
        self.MOVE_DISTANCE = 1  # 아이템이 반복문에서 한 번의 반복마다 움직이는 거리

        self.xPos = random.randint(50, 650)
        self.yPos = 100
        self.advance = advance  # advance가 True라면, 오른쪽으로 움직일 차례임을 나타냄
        self.itemName = itemName    # 아이템 이름

        self.itemImage = makeSprite("./images/item/" + itemName + ".png")
        moveSprite(self.itemImage, self.xPos, self.yPos)
        showSprite(self.itemImage)
        itemList.append(self)

    def itemMove(self, itemList, snitch):
        cur_movement = self.MOVE_DISTANCE if self.advance else - self.MOVE_DISTANCE
        self.xPos += cur_movement
        self.yPos += 1  # 대각선으로 이동시키기 위해 X좌표와 Y좌표 모두 이동 시킴

        moveSprite(self.itemImage, self.xPos, self.yPos, True, False)  # 아이템을 이동시킴

        if self.yPos > 700 :    # 아이템이 맵 밖으로 나가면, 이미지를 지움
            hideSprite(self.itemImage)
            itemList.remove(self)
        elif self.xPos < 50 or self.xPos > 650:  # 왔다 갔다 할 수 있도록 진행 방향을 바꿔줌 (벽을 만나면 진행 방향을 바꿈)
            self.advance = not self.advance  # 진행 방향 바꿈

class Label:
    def __init__(self, labelList, timeLimit, member, text, fontSize, xpos, ypos, fontColour='black', font='Arial', background="clear", is_italic = False):
        self.creationTime = pygame.time.get_ticks() # label이 생성된 시간
        self.timeLimit = timeLimit # label 유지 시간
        self.member = member
        self.label = makeLabel(text, fontSize, xpos, ypos, fontColour, font, background, is_italic)

        for label in labelList: # 라벨이 겹치지 않도록 기존에 있던 라벨을 숨김
            if label.member != self.member:
                hideLabel(label.label)

        showLabel(self.label)
        labelList.append(self)

    def removeTimeoutLable(self, labelList):  # 지정된 시간이 되면 lable을 지움
        curTime = pygame.time.get_ticks()   # 현재 시각

        if self.creationTime + self.timeLimit * 1000 < curTime :    # label 유지시간이 지난 경우
            hideLabel(self.label)   # lable을 가림
            labelList.remove(self)  # list에서 lable을 제거

def drawHealth(healthBarList, healthPath, startHealthValue, endHealthValue, x, y, interval, screenRefresh = False): # 체력을 그리는 함수
    setAutoUpdate(screenRefresh)    # 체력이 조금씩 증가하는 것을 보여주려면 screenRefresh를 True로 하여 함수를 호출
    for health1 in range(startHealthValue, endHealthValue):
        healthImage = makeSprite(healthPath)
        moveSprite(healthImage, health1 + x + interval, y + interval)
        showSprite(healthImage)
        healthBarList.append(healthImage)
    setAutoUpdate(False)


def game():
    ######### 맵 초기화 #########
    setBackgroundImage("./images/background/map2.png")   # 이차원 배열을 사용하여 grid 배경을 만들어줄 수 있음.
    #############################

    ################## 캐릭터, 몬스터 초기화 ##################
    player = Player("./images/character/quidditchPotter.png", 350, 530)

    monsterList = []
    monster1 = Monster(monsterList, "./images/monster/dementor.gif", 100, 40, True, 5, 10)
    monster2 = Monster(monsterList, "./images/monster/dementor.gif", 600, 40, False, 5, 10)
    ###############################################

    ############ 플레이어 상태창 초기화 ############
    player.healthBar = makeSprite("./images/status/smallHealthbar.png")
    moveSprite(player.healthBar, player.xPos - 27, player.yPos - 12)
    showSprite(player.healthBar)
    drawHealth(player.healthBarList, "./images/status/smallHealth.png", 0, player.health, player.xPos - 27, player.yPos - 12, 2, True)  # 최초 플레이어의 체력을 화면에 표시
    setAutoUpdate(False)
    ################################################

    ############ 몬스터 상태창 초기화 ############
    monster1.statusInit()
    monster2.statusInit()
    #######################################

    time_limit = 60500  # game2의 제한시간
    gameStartTime = pygame.time.get_ticks() # 게임 시작 시간
    beforeMinute = '0' + str((time_limit + gameStartTime - pygame.time.get_ticks()) // 60000)    # 시간은 항상 10분 이내이므로 minutes 앞에 0을 붙여줌
    beforeSecond = str((time_limit + gameStartTime - pygame.time.get_ticks()) // 1000 % 60)

    labelList = []
    opponentList = []
    itemList = []
    clockLabel = None
    magic = None

    pauseStartTime = None   # pause가 시작된 시간 (모래시계를 획득하면 pause됨)
    is_pause = False    # 현재 pause 중인지를 나타냄
    isKill = False  # 디멘터를 모두 처치했는지를 나타냄
    isAcquire = False   # 스니치를 획득했는지를 나타냄
    snitch = None   # 스니치 아이템 객체를 저장할 변수

    while not player.gameOver:
        if is_pause == False :  # 모래시계 아이템의 효과인 일시정지 중에는 플레이어만 움직일 수 있도록 함
            curMinute = '0' + str((time_limit + gameStartTime - pygame.time.get_ticks()) // 60000)  # 시간은 항상 10분 이내이므로 minutes 앞에 0을 붙여줌
            curSecond = str((time_limit + gameStartTime - pygame.time.get_ticks()) // 1000 % 60)

            # 주어진 시간 내에 게임을 클리어하지 못한 경우 또는 플레이어의 체력을 모두 소모한 경우 또는 스니치를 획득하고 디멘터를 모두 처치한 경우
            if (player.health <= 0) or (curMinute == "00" and curSecond == "0") or (isAcquire == True and isKill == True):
                # 주어진 시간 내에 게임을 클리어하지 못한 경우 또는 플레이어의 체력을 모두 소모한 경우엔 게임 결과 code를 0으로 설정하여, 게임을 클리어하지 못했음을 나타냄
                if (player.health <= 0) or (curMinute == "00" and curSecond == "0") :
                    if isKill == False and isAcquire == False:  # 스니치도 얻지 못하고, 디멘터도 모두 처치하지 못한 경우
                        player.gameResult = 0
                    elif isKill == False and isAcquire == True: # 스니치를 얻었지만, 디멘터를 모두 처치하지 못한 경우
                        player.gameResult = 1
                    elif isKill == True and isAcquire == False: # 디멘터를 모두 처치하였지만, 스니치를 얻지 못한 경우
                        player.gameResult = 2
                elif isAcquire == True and isKill == True:  # 게임을 클리어한 경우
                    player.gameResult = 3
                player.gameOver = True  # while문을 빠져나가기 위해 gameOver flag의 값을 True로 수정
                continue

            # 상대 팀원과 캐릭터가 충돌했는지 확인
            for opponent in opponentList:
                if touching(player.character, opponent.opponent):   # 플레이어가 상대 팀원과 충돌하였다면
                    if not opponent in player.touchingOpponentList:
                        player.touchingOpponentList.append(opponent)
                        player.health -= opponent.POWER  # 플레이어의 체력이 1 감소함
                        for i in range(0, opponent.POWER):
                            if (len(player.healthBarList) != 0):
                                hideSprite(player.healthBarList.pop())  # 플레이어의 체력이 감소하는 것을 표현하기 위해 체력바의 가장 마지막 이미지를 숨김.
                                updateDisplay()

                        if player.isBig == False : # 플레이어가 상대 팀원과 충돌하여 몸집이 커지는 효과가 적용중이지 않은 경우에는 effect 0, 1 모두 적용될 수 있음
                            effect = random.randint(0, 1)   # 상대 팀원 이미지와 충돌 시 효과
                        else :
                            effect = 1  # 플레이어가 몸집이 커지는 효과를 얻은 경우엔 남은 시간이 줄어드는 효과만 얻을 수 있음

                        member = random.randint(0, 1000)
                        Label(labelList, 1.5, member, "충돌 패널티", 25, 280, 250, "red", FONT_FAMILY)
                        if effect == 0:
                            # 캐릭터의 몸집이 5초간 커짐.
                            changeSpriteImage(player.character, 1)
                            player.isBig = True
                            player.playerMove()

                            Label(labelList, 1.5, member, "캐릭터의 몸집이 10초간 커집니다!", 25, 160, 300, "red", FONT_FAMILY)
                            player.bigImageStartTime = pygame.time.get_ticks()
                        elif effect == 1:
                            # 남은 시간이 줄어듦.
                            time_limit -= 1000
                            Label(labelList, 1.5, member, "남은 시간이 1초 줄어듭니다!", 25, 190, 300, "red", FONT_FAMILY)


            for item in itemList:
                if touching(player.character, item.itemImage):  # 플레이어가 아이템을 획득한 경우
                    if item.itemName == "hourglass":    # 획득한 아이템이 모래시계인 경우
                        is_pause = True
                        time_limit += 1500  # limit을 늘려서 시간을 멈춘 효과를 냄
                        pauseStartTime = pygame.time.get_ticks()    # 일시정지 효과가 시작된 시간을 기록
                        increseHealthValue = 10 # increseHealthValue만큼 플레이어의 체력을 증가시킴
                        Label(labelList, 1.5, 1001, "모래시계를 획득하여 시간을 멈추고 체력을 회복합니다!", 25, 25, 300, "white",FONT_FAMILY)

                        if(player.MAX_HEALTH - player.health < increseHealthValue):   # 현재까지 감소한 체력이 증가할 체력보다 낮으면
                            increseHealthValue = player.MAX_HEALTH - player.health

                        if player.isBig == True:
                            drawHealth(player.healthBarList,"./images/status/smallHealth.png", player.health, player.health + increseHealthValue, player.xPos - 17, player.yPos - 12, 2, True)  # 최초 플레이어의 체력을 화면에 표시
                        else :
                            drawHealth(player.healthBarList,"./images/status/smallHealth.png", player.health, player.health + increseHealthValue, player.xPos - 27, player.yPos - 12, 2, True)  # 최초 플레이어의 체력을 화면에 표시

                        player.health += increseHealthValue
                    elif item.itemName == "snitch": # 획득한 아이템이 스니치인 경우
                        Label(labelList, 5, 1002, "스니치 획득, 퀴디치 게임 종료!", 25, 165, 300, "white", FONT_FAMILY)
                        isAcquire = True    # 플레이어가 스니치를 획득했음을 나타내는 flag를 True로 설정

                        for opponent in opponentList:   # 상대 팀원 이미지를 모두 숨김
                            hideSprite(opponent.opponent)

                        opponentList.clear()    # 상대 팀원을 list에서 모두 제거

                    hideSprite(item.itemImage)  # 획득한 아이템의 이미지를 숨김
                    itemList.remove(item)   # 획득한 아이템을 list에서 제거

            # 몬스터 이미지와 마법 이미지가 겹치면 몬스터의 체력을 감소시킴
            if (magic != None):
                for monster in monsterList:
                    if (magic != monster.touchingMagic):  # 새롭게 시전된 마법이라면
                        monster.touchingMagic = magic
                        if touching(monster.image, magic.image):    # 몬스터가 마법에 맞은 경우
                            for i in range(0, magic.POWER):  # 몬스터의 체력을 감소 시킴
                                monster.health -= 1
                                if (len(monster.healthBarList) != 0):
                                    hideSprite(monster.healthBarList.pop())  # 몬스터의 체력이 감소하는 것을 표현하기 위해 감소폭만큼 체력바의 이미지를 가린다.
                        if (monster.health <= 0):    # 몬스터의 체력을 모두 소모한 경우
                            hideSprite(monster.image)   # 몬스터 이미지를 숨김

            if magic != None:  # 마법을 시전중인 경우
                if magic.is_timeout() == True:  # 시전 중인 마법의 시전 시간이 끝난 경우
                    hideSprite(magic.image)  # 마법 이미지를 가림
                    hideLabel(magic.magicLabel)  # 마법 Label을 가림
                    magic = None  # 마법이 시전중이지 않음을 나타내기 위해 None으로 바꿈 (마법은 한번에 한개만 사용할 수 있음)

            if snitch == None and isAcquire == False and (curSecond == "5" or curSecond == "15"):  # 스니치를 획득하지 못한 경우 남은 시간이 15초 또는 5초일 때, rand 함수를 사용하여 스니치 아이템을 생성
                snitch = Item(itemList, "snitch", random.randint(0, 1))

            if random.randint(0, 1800) == 0:
                Item(itemList, "hourglass", random.randint(0, 1))  # rand 함수를 사용하여 모래시계 아이템을 랜덤하게 생성

            if isAcquire == False and random.randint(0, 150) == 0:  # 스니치를 획득하지 못한 경우, rand 함수를 사용하여 플레이어의 캐릭터에게 돌진하는 상대팀을 랜덤하게 생성
                Opponent(opponentList, random.randint(0, 1))

            if snitch != None and snitch.yPos > 700 :   # 스니치가 맵 밖으로 나가면 snitch를 None으로 바꿈
                snitch = None

            if len(monsterList) == 0:
                isKill = True  # 몬스터를 모두 처치했음을 나타내는 flag를 True로 설정

            if player.isBig == True :
                player.timeToSmall()

            for monster in monsterList :
                if(monster.health <= 0):
                    monster.extinctionMotion(monsterList, labelList)    # 디멘터 소멸 모션

            for monster in monsterList:
                if monster.health > 0:
                    monster.monsterMove()  # 디멘터를 움직임

            for item in itemList:
                item.itemMove(itemList, snitch) # 각 아이템을 움직임

            for opponent in opponentList:
                opponent.opponentMove(opponentList) # 퀴디치 상대 팀원을 움직임

            for label in labelList:
                label.removeTimeoutLable(labelList)

            if (int(beforeSecond) > int(curSecond) or int(beforeMinute) > int(curMinute)):  # 표시할 시간이 줄어들었을 때만 시간을 바꿈 (조건 없이 while문을 반복하면서 시간을 바꿔주면 시간이 너무 깜빡거림)
                # 시간 update
                hideLabel(clockLabel)  # 기존의 시간을 숨김
                if (int(curSecond) < 10): curSecond = '0' + curSecond  # 9초 이하에는 '0'을 붙여줌

                if (int(curSecond) < 10 and int(curMinute) == 0):  # 남은 시간이 10초 이하라면, 시간을 표시하는 label의 색을 red로 표현
                    clockLabel = makeLabel(curMinute + ":" + curSecond, 45, 290, 20, "red", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.
                else:
                    clockLabel = makeLabel(curMinute + ":" + curSecond, 45, 290, 20, "white", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.

                showLabel(clockLabel)  # 시간을 화면에 표시
                # 현재 시간을 갱신
                beforeMinute = curMinute
                beforeSecond = curSecond
                player.health -= player.HEALTH_REDUCTION_WIDTH  # 플레이어의 체력은 1초당 healthRedunctionWidth(체력 감소 폭)씩 감소
                for i in range(0, player.HEALTH_REDUCTION_WIDTH):
                    if (len(player.healthBarList) != 0):
                        hideSprite(player.healthBarList.pop())  # 플레이어의 체력이 감소하는 것을 표현하기 위해 감소폭만큼 체력바의 이미지를 숨김.

            if keyPressed("q"):  # q를 누른 경우
                if magic == None:  # 현재 마법을 시전 중이지 않다면
                    magic = Magic(player.xPos + 10, 55)  # 마법을 생성

        if is_pause == True and (pygame.time.get_ticks() - pauseStartTime) > 1500:  # 모래시계 아이템의 일시정지 효과의 시간이 끝난 경우
            is_pause = False    # 현재 일시정지 상태인지를 나타내는 flag를 False로 설정

        # 일시정지 상태여도 플레이어는 움직일 수 있음
        if keyPressed("right"):
            player.playerMove("right")
        elif keyPressed("left"):
            player.playerMove("left")

        updateDisplay() # 화면 update를 자동으로 하지 않기 때문에 반복문의 가장 마지막에 화면을 update 함
        tick(120)

    # while문을 벗어난 경우
    for label in textboxGroup :    # 모든 라벨을 가림
        hideLabel(label)

    hideAll()   # 모든 sprite 숨김.

    # 게임 결과에 따라 이미지를 출력
    if player.gameResult != 0 :
        gameOverImage = makeSprite("./images/story/ending/ending" + str(player.gameResult) + ".png")
        showSprite(gameOverImage)
        pause(2000)
    elif player.gameResult == 0:
        gameOverImage = makeSprite("./images/result/fail.png")
        showSprite(gameOverImage)
        pause(2000)
        hideAll()  # 모든 sprite 숨김.

    return player.gameResult    # 게임 결과를 반환