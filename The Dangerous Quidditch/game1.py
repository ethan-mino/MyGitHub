# -*- coding: UTF-8 -*-

import pygame as pygame
import random
import time
from pygame_functions import * # pygame_functions 튜토리얼 (https://www.youtube.com/watch?v=gmBW_AVDsNY&feature=youtu.be&list=PLeOSHd3t9lzKr4O3A3Q7OZyf8QwyCALyn 참조)
                               # pygame_functions git hub (https://github.com/StevePaget/Pygame_Functions 참조)
                               # pygame을 쉽게 사용 가능.

FRAME_UPDATE_INTERVAL = 100  # 프레임이 업데이트 되는 간격
PLAYER_TOTAL_FRAME = 8  # 프레임 개수
TIME_LIMIT = 60000  # game1의 제한시간 (1분 30초)
WINDOW_POS = [190, 150] # 게임 최초 시작시 맵 상에서 게임창의 좌측 상단의 좌표
SCROLL_SPEED = 3  # 배경화면이 스크롤 되는 속도
TOTAL_ITEM_NUM = 5  # 총 아이템 개수
EFFECT_TIME = 4 # 몬스터와 충돌 시 얻는 effect의 지속 시간
FONT_FAMILY = "./font/neodgm.ttf"
LOSE_CODE = 0    # 제한시간 내에 체력을 모두 소모하지 않고 아이템을 모두 수집한 경우 게임 결과 code
CLEAR_CODE = 1
itemLabel = None    # 캐릭터가 수집한 아이템의 개수를 나타내는 label

class Monster :
    def __init__(self, monsterList, imagePath, x, y, direction, distance, advance, directionFrame, totalFrame = 0) :
        self.monsterX = x  # 몬스터의 첫 x좌표
        self.monsterY = y  # 몬스터의 첫 y좌표
        self.direction = direction  # 몬스터의 이동 방향 (상하 or 좌우)
        self.advance  = advance    # 몬스터가 죄우로 움직일 때 advance가 True라면, 오른쪽으로 움직일 차례임을 나타내고, 몬스터가 상하로 움직일 때 advance가 True라면, 위로 움직일 차례임을 나타냄
        self.move = 0   # 현재 진행 방향으로 움직인 횟수(거리)를 나타냄
        self.nextFrame = clock()  # 다음 프레임이 나타나는 시간
        self.frame = 0  # 프레임 번호
        self.DISTANCE = distance  # 몬스터가 이동하는 구간의 길이
        self.MONSTER_DIRECTION_FRAME = directionFrame  # 몬스터를 표현하는 프레임 개수 (방향당 8개)

        self.monsterImage = makeSprite(imagePath, totalFrame) # 몬스터를 만듦
        moveSprite(self.monsterImage, self.monsterX, self.monsterY) # 몬스터를 좌표에 위치시킴
        showSprite(self.monsterImage) # 몬스터를 화면에 표시
        monsterList.append(self)    #몬스터 list에 추가

    def scrollMonster(self, row, col):  # 배경이 스크롤되는 것에 맞춰 몬스터도 같이 스크롤하여 멈춰있는 효과를 내기 위한 함수
        self.monsterX += row
        self.monsterY += col

        moveSprite(self.monsterImage, self.monsterX, self.monsterY, False, False)  # 몬스터의 위치를 이동시킴.

    def moveMonster(self):  # 몬스터를 self.direction가 나타내는 방향으로 self.distance만큼 반복하여 왔다 갔다 이동시키는 함수
        global FRAME_UPDATE_INTERVAL    # 몬스터의 Frame을 update 하는 간격

        if clock() > self.nextFrame:  # FRAME_UPDATE_INTERVAL마다 몬스터의 frame을 update함
            self.frame = (self.frame + 1) % self.MONSTER_DIRECTION_FRAME  # 몬스터의 frame 번호를 갱신 (modulus를 사용하여 frame 번호가 0 ~ self.MONSTER_TOTAL_FRAME -1을 반복하도록 함)
            self.nextFrame += FRAME_UPDATE_INTERVAL  # 다음 프레임이 나타나는 시간(nextFrame)이 되면 frame 번호를 갱신 -> frame을 갱신하지 않으면 몬스터의 동작이 바뀌지 않고, nextFrame을 갱신하지 않으면 동작이 너무 빨리 바뀜.

            # 캐릭터가 움직이는 방향에 맞춰 이미지를 바꿔줌
            if self.advance == True:
                changeSpriteImage(self.monsterImage, 1 * self.MONSTER_DIRECTION_FRAME + self.frame)
            if self.advance == False:
                changeSpriteImage(self.monsterImage, 0 * self.MONSTER_DIRECTION_FRAME + self.frame)

        if self.direction == "row": # 진행 방향이 row라면, 몬스터의 x좌표에 1을 더해주고, col이라면 y좌표에 1을 더해줌
            self.monsterX += 1 if self.advance else -1
        elif self.direction == "col":
            self.monsterY += 1 if self.advance else -1

        moveSprite(self.monsterImage, self.monsterX, self.monsterY, False, False)   # 몬스터를 이동시킴
        self.move += 1  # 몬스터가 진행 방향으로 움직인 거리를 1 증가시킴

        if self.move == self.DISTANCE:  # 왔다 갔다 할 수 있도록 진행 방향을 바꿔줌
            self.move = 0   # 움직인 거리는 0으로 값을 바꿈
            self.advance = not self.advance # 진행 방향 바꿈

class Player :
    def __init__(self, imagePath, frame, centerXpos, centerYpos) :
        self.gameOver = False   # 게임이 종료되었는지를 나타내는 flag (while문을 벗어나는데 사용된다)
        self.health = 195   # 플레이어의 체력
        self.itemNum = 0    # 플레이어가 획득한 아이템 개수
        self.healthBarList = []
        self.gameResult = 0 # 게임을 클리어 하지 못한 경우 gameResult는 0, 클리어한 경우는 1
        # player의 좌표는 맵 상에서의 위치를 나타낸다.
        self.centerPos = [0,0]  # 일단 캐릭터 이미지의 centerPos는 (0,0)으로 설정해준 후, updatePos함수에서 midLeftPos, midRightPos, bottomLeftPos, bottomRightPos를 수정해줌
        self.width = 100    # 캐릭터 이미지의 가로 길이
        self.height = 100   # 캐릭터 이미지의 세로 길이
        self.updatePos(centerXpos + WINDOW_POS[0], centerXpos + WINDOW_POS[1])  # 맵의 시작 점과 게임 창의 시작 점이 다르므로, 게임 창의 좌표를 현재 캐릭터의 화면상 좌표에 더해준다.
        self.character = makeSprite(imagePath, frame)  # 32개의 frame을 포함하는 gif파일. # 두번째 파라미터는 gif 파일이 포함하는 frame의 개수를 나타냄. links.gif contains 32 separate frames of animation. ()
                                                       # 컴퓨터가 스프라이트 시트를 통해 각 프레임을 개별적으로 선택하므로 어디에서 찾을 지 알 수 있도록 균등한 간격을 두어야한다.
        changeSpriteImage(self.character, 1 * PLAYER_TOTAL_FRAME + 5)  # 아무것도 누르지 않았을 땐 캐릭터가 앞을 보게함.

        moveSprite(self.character, centerXpos, centerYpos, True)  # 게임 캐릭터는 화면 가운데에 위치.
        showSprite(self.character)  # 캐릭터를 화면에 출력

    def updatePos(self, x, y):    # 맵 위의 캐릭터의 좌표를 수정
        self.centerPos[0] += x
        self.centerPos[1] += y

        # 캐릭터의 중앙 좌표 값이 바뀜에 따라 midLeftPos, midRightPos, bottomLeftPos, bottomRightPos를 수정해줌
        self.midLeftPos = [self.centerPos[0] - int(self.width/2), self.centerPos[1]]   # 캐릭터 중앙 좌측 좌표
        self.midRightPos = [self.centerPos[0] + int(self.width/2), self.centerPos[1]]  # 캐릭터 중앙 우측 좌표
        self.bottomLeftPos = [self.centerPos[0]  - int(self.width/2), self.centerPos[1] + int(self.height /2)]  # 캐릭터 하단 좌측 좌표
        self.bottomRightPos = [self.centerPos[0]  + int(self.width/2), self.centerPos[1] + int(self.height /2)] # 캐릭터 하단 우측 좌표

    def is_colision(self, obstacleList, monsterList) :
        # 캐릭터와 맵 외부와의 충돌 감지 (각 논리는 차례대로 왼쪽 벽, 오른쪽 벽, 상단 벽, 하단 벽과의 충돌을 감지한다)
        if(self.midLeftPos[0] < -30 or self.midRightPos[0] > 1030 or self.midLeftPos[1] < 0  or self.bottomLeftPos[1] > 1010):
            return True
        else:   # 캐릭터와 맵의 장애물의 충돌 감지
            for obstacle in obstacleList:
                if(self.midRightPos[0] >= obstacle.topLeftPos[0] and self.midLeftPos[0] <= obstacle.topRightPos[0] and  # https://progdev.tistory.com/8 참조
                self.bottomLeftPos[1] >= obstacle.topLeftPos[1] and self.midLeftPos[1] <= obstacle.bottomRightPos[1]):
                        return True
            return False

class Obstacle:
    def __init__(self, obstacleList, topLeftXpos, topLeftYpos, width, height):
        self.width = width   # 플레이어가 지나갈 수 없는 장애물의 너비
        self.height = height    # 장애물의 높이

        # 장애물의 맵 상에서의 좌표 (플레이어와의 충돌 여부를 확인하는데 사용된다)
        self.topLeftPos = [topLeftXpos, topLeftYpos]
        self.topRightPos = [self.topLeftPos[0] + self.width, self.topLeftPos[1]]
        self.bottomLeftPos = [self.topLeftPos[0], self.topLeftPos[1] + self.height]
        self.bottomRightPos = [self.topLeftPos[0] + self.width, self.topLeftPos[1] + self.height]

        obstacleList.append(self)   # 장애물 list에 append

class Item :
    def __init__(self, imagePath, xPos, yPos, itemList):
        self.xPos = xPos
        self.yPos = yPos

        self.itemImage = makeSprite(imagePath)
        moveSprite(self.itemImage, xPos, yPos)
        showSprite(self.itemImage)
        itemList.append(self)

    def scrollItem(self, row, col):  # 배경이 스크롤되는 것에 맞춰 몬스터도 같이 스크롤하여 멈춰있는 효과를 내기 위한 함수
        self.xPos += row
        self.yPos += col
        moveSprite(self.itemImage, self.xPos, self.yPos, False, False)  # 몬스터의 위치를 이동시킴.

def drawHealthBar(healthBarList, healthValue):
    healthbar = makeSprite("./images/status/bigHealthbar.png")
    moveSprite(healthbar, 462, 80)
    showSprite(healthbar)

    for health1 in range(0, healthValue):
        healthImage = makeSprite("./images/status/bigHealth.png")
        moveSprite(healthImage, health1 + 465, 83)
        showSprite(healthImage)
        healthBarList.append(healthImage)

def scrollGameObj(player, frame, frame_index, scrollXpos, scrollYpos, monsterList, obstacleList, itemList, curEffect): # 플레이어가 방향키를 눌러 움직일 때 호출되는 함수
    changeSpriteImage(player.character, frame_index * PLAYER_TOTAL_FRAME + frame)  # 방향에 맞게 캐릭터의 이미지를 바꿔줌

    player.updatePos(-scrollXpos, -scrollYpos)  # 캐릭터는 배경과 반대로 움직이므로 scrollXpos와 scrollYpos 마이너스를 곱한 값을 더해줌

    if (not player.is_colision(obstacleList, monsterList)):  # 장애물과 충돌하지 않은 경우
        scrollBackground(scrollXpos, scrollYpos)    # 배경을 scroll
        global itemLabel

        for item in itemList:
            item.scrollItem(scrollXpos, scrollYpos) # 각 item들도 scroll
            if(touching(player.character, item.itemImage)): # 캐릭터가 item과 충돌한 경우
                hideSprite(item.itemImage)  # 아이템의 이미지를 숨김
                player.itemNum += 1 # 플레이어가 획득한 아이템의 개수를 1 증가시킴
                itemList.remove(item)   # 획득한 아이템을 item 리스트에서 제거
                hideLabel(itemLabel)
                if(curEffect != 1):  # 플레이어가 몬스터에 충돌하여 암흑 효과를 얻은 경우 itemLabel을 표시하지 않음
                    itemLabel = makeLabel(" : " + str(player.itemNum) + " / " + str(TOTAL_ITEM_NUM), 30, 432, 120, "white", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.
                    showLabel(itemLabel)  # 현재 수집한 아이템 개수를 화면에 표시

        for monster in monsterList:
            monster.scrollMonster(scrollXpos, scrollYpos)   # 각 몬스터의 위치를 scroll
    else:
        player.updatePos(scrollXpos, scrollYpos)  # 캐릭터와 장애물이 충돌한 경우, update 했던 값을 복구함 (제자리에서 움직이는 이미지만 바꿔줌)

def game() :
    ############## 맵 초기화 ##############
    black = "images/background/black.png"
    setBackgroundImage([[black, black, black], [black, "images/background/map1.png", black], [black, black, black]])   # 이차원 배열을 사용하여 grid 배경을 만들어줄 수 있음.
    scrollBackground(-1000 - WINDOW_POS[0], -1000 - WINDOW_POS[1])    # 맵의 중앙으로 scroll
    #######################################

    ################## 캐릭터, 몬스터, 장애물, 아이템 초기화 ##################
    player = Player("images/character/potter.gif", 32, 350, 350)  # 32개의 frame을 포함하는 gif파일. # 두번째 파라미터는 gif 파일이 포함하는 frame의 개수를 나타냄.
                                                       # 컴퓨터가 스프라이트 시트를 통해 각 프레임을 개별적으로 선택하므로 어디에서 찾을 지 알 수 있도록 균등한 간격을 두어야한다.

    monsterList = []    # monster들을 append할 list
    monster1 = Monster(monsterList, "images/monster/monster2.gif", 70, -135, "row", 160, True, 6, 12)  # 좌우로 움직이는 몬스터
    monster2 = Monster(monsterList, "images/monster/monster2.gif", 400, 580, "row", 250, False, 6, 12)
    monster2 = Monster(monsterList, "images/monster/monster3.gif", 560, 370, "row", 160, True, 6, 12)
    monster3 = Monster(monsterList, "images/monster/monster1.gif", -130, 390, "col", 100, True, 6, 12)  # 상하로 움직이는 몬스터
    monster4 = Monster(monsterList, "images/monster/monster1.gif", 620, -40, "col", 150, False, 6, 12)

    obstacleList = []   # 장애물들을 append할 list
    obstacle1 = Obstacle(obstacleList, 100, 75, 130, 100)
    obstacle2 = Obstacle(obstacleList, 229, 185, 1, 150)
    obstacle3 = Obstacle(obstacleList, 230, 250, 120, 90)
    obstacle4 = Obstacle(obstacleList, 350, 250, 160, 25)
    obstacle5 = Obstacle(obstacleList, 480, 150, 30, 100)
    obstacle6 = Obstacle(obstacleList, 440, 150, 40, 30)
    obstacle7 = Obstacle(obstacleList, 380, 115, 1, 65)
    obstacle8 = Obstacle(obstacleList, 380, 0, 1, 30)
    obstacle9 = Obstacle(obstacleList, 520, 0, 265, 40)
    obstacle10 = Obstacle(obstacleList, 650, 40, 40, 200)
    obstacle11 = Obstacle(obstacleList, 750, 135, 35, 105)
    obstacle12 = Obstacle(obstacleList, 930, 0, 70, 105)
    obstacle13 = Obstacle(obstacleList, 930, 200, 70, 180)
    obstacle14 = Obstacle(obstacleList, 620, 350, 310, 30)
    obstacle15 = Obstacle(obstacleList, 620, 380, 40, 350)
    obstacle16 = Obstacle(obstacleList, 660, 480, 250, 70)
    obstacle17 = Obstacle(obstacleList, 660, 660, 150, 70)
    obstacle18 = Obstacle(obstacleList, 950, 660, 50, 190)
    obstacle19 = Obstacle(obstacleList, 600, 840, 350, 10)
    obstacle20 = Obstacle(obstacleList, 600, 860, 150, 60)
    obstacle21 = Obstacle(obstacleList, 880, 940, 120, 60)
    obstacle22 = Obstacle(obstacleList, 330, 840, 120, 160)
    obstacle23 = Obstacle(obstacleList, 80, 890, 100, 20)
    obstacle24 = Obstacle(obstacleList, 160, 775, 20, 115)
    obstacle25 = Obstacle(obstacleList, 0, 775, 160, 20)
    obstacle26 = Obstacle(obstacleList, 0, 280, 40, 495)
    obstacle27 = Obstacle(obstacleList, 40, 280, 40, 150)
    obstacle28 = Obstacle(obstacleList, 40, 430, 140, 90)
    obstacle29 = Obstacle(obstacleList, 330, 430, 110, 300)
    obstacle30 = Obstacle(obstacleList, 200, 630, 140, 40)

    itemList = []
    item1 = Item("./images/item/snitch.png", -130, 660, itemList)
    item2 = Item("./images/item/snitch.png", 180, 30, itemList)
    item3 = Item("./images/item/snitch.png", 500, 255, itemList)
    item4 = Item("./images/item/snitch.png", 750, -30, itemList)
    item5 = Item("./images/item/snitch.png", 750, 715, itemList)
    ########################################################################

    # 몬스터 충돌 시 화면에 나타날 효과 fire, dark
    fire = makeSprite("./images/background/fire.png")  # 불 이미지
    moveSprite(fire, 0, 520)    # 불 이미지를 화면 하단으로 옮김

    dark = makeSprite("./images/background/dark.png")

    ############ 상태창 초기화 ############
    global itemLabel
    itemLabel = makeLabel(" : " + str(player.itemNum) + " / " + str(TOTAL_ITEM_NUM), 30, 432, 120, "white", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.
    showLabel(itemLabel)  # 현재 수집한 아이템 개수를 화면에 표시

    hpLabel = makeLabel("HP: ", 35, 410, 75, "white", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.
    showLabel(hpLabel)  # 현재 수집한 아이템 개수를 화면에 표시

    harryImage = makeSprite("./images/status/harry.png")
    moveSprite(harryImage, 530, 10)
    showSprite(harryImage)

    smallSnitch = makeSprite("./images/item/smallSnitch.png")
    moveSprite(smallSnitch, 408, 115)
    showSprite(smallSnitch)

    drawHealthBar(player.healthBarList, player.health)  # 최초 플레이어의 체력을 화면에 표시
    #########################################

    ############# 몬스터 충돌시 화면에 나타날 메시지 초기화 #############
    darkLabelList = []
    darkLabelList.append(makeLabel("신비한 동물과 부딪혀", 25, 220, 150, "white", FONT_FAMILY))    # 몬스터와 충돌하여 dark effect를 얻은 경우 출력할 label
    darkLabelList.append(makeLabel("5초간 암흑에 둘러쌓입니다.", 25, 190, 200, "white", FONT_FAMILY))

    fireLabelList = []
    fireLabelList.append(makeLabel("신비한 동물과 부딪혀", 25, 220, 160, "white", FONT_FAMILY))     # 몬스터와 충돌하여 fire effect를 얻은 경우 출력할 label
    fireLabelList.append(makeLabel("5초간 화염에 둘러쌓임과 동시에", 25, 160, 210, "white", FONT_FAMILY))     # 몬스터와 충돌하여 fire effect를 얻은 경우 출력할 label
    fireLabelList.append(makeLabel("체력이 빠르게 소모됩니다.", 25, 190, 260, "white", FONT_FAMILY))     # 몬스터와 충돌하여 fire effect를 얻은 경우 출력할 label

    ####################################################################

    ################## 게임에 필요한 데이터 ##################
    nextFrame = clock() # 다음 프레임이 나타나는 시간.
    frame = 0  # 프레임 번호
    effect = 0
    effectStartTime = 0
    clockLabel = None
    gameStartTime = pygame.time.get_ticks() # 게임 시작 시간
    beforeMinute = '0' + str((TIME_LIMIT + gameStartTime - pygame.time.get_ticks()) // 60000)    # 시간은 항상 10분 이내이므로 minutes 앞에 0을 붙여줌
    beforeSecond = str((TIME_LIMIT + gameStartTime - pygame.time.get_ticks()) // 1000 % 60)
    healthRedunctionWidth = 1  # 초당 감소하는 플레이어의 체력
    ##########################################################
    setAutoUpdate(False)

    while not player.gameOver:
        curMinute = '0' + str((TIME_LIMIT + gameStartTime - pygame.time.get_ticks()) // 60000)   # 시간은 항상 10분 이내이므로 minutes 앞에 0을 붙여줌
        curSecond = str((TIME_LIMIT + gameStartTime - pygame.time.get_ticks()) // 1000 % 60)

        # 주어진 시간 내에 게임을 클리어하지 못한 경우 또는 플레이어의 체력을 모두 소모한 경우 또는 골든 스니치를 모두 수집한 경우
        if (player.health <= 0) or (curMinute == "00" and curSecond == "0") or (player.itemNum == TOTAL_ITEM_NUM):
            # 주어진 시간 내에 게임을 클리어하지 못한 경우 또는 플레이어의 체력을 모두 소모한 경우엔 게임 결과 code를 0으로 설정하여, 게임을 클리어하지 못했음을 나타냄
            if(player.health <= 0) or (curMinute == "00" and curSecond == "0") :
                player.gameResult = LOSE_CODE
            else:   # 제한시간내에 골든 스니치를 모두 수집한 경우 게임 결과 code를 1로 설정하여 게임을 클리어했음을 나타냄
                player.gameResult = CLEAR_CODE
            player.gameOver = True  #while문을 빠져나가기 위해 gameOver flag의 값을 True로 수정
            continue

        if clock() > nextFrame:                         # We only animate our character every 80ms.
            frame = (frame + 1) % PLAYER_TOTAL_FRAME    # There are 8 frames of animation in each direction
            nextFrame += FRAME_UPDATE_INTERVAL          # 다음 프레임이 나타나는 시간(nextFrame)이 되면 frame 번호를 갱신 -> frame을 갱신하지 않으면 동작이 바뀌지 않고, nextFrame을 갱신하지 않으면 동작이 너무 빨리 바뀜. # so the modulus 8 allows it to loop

        for monster in monsterList:
            if (touching(player.character, monster.monsterImage)):  # 캐릭터가 몬스터와 충돌한 경우
                player.health -= 1  # 몬스터와 접촉하는 동안 체력이 1 소모함
                if(len(player.healthBarList) != 0):
                    hideSprite(player.healthBarList.pop()) # 플레이어의 체력이 감소하는 것을 표현하기 위해 체력바의 가장 마지막 이미지를 가린다.
                if effect == 0: # effect가 적용되고 있지 않다면
                    effect = random.randint(1, 2)    # 몬스터와 부딪힐 때 얻는 effect 번호
                    if effect == 1:  # 캐릭터가 암흑으로 둘러쌓임
                        showSprite(dark)
                        hideLabel(hpLabel)
                        hideLabel(itemLabel)
                        for darkLabel in darkLabelList:
                            showLabel(darkLabel)
                    elif effect == 2:  # 불 이미지가 나타나는 효과, 체력이 더 빨리 닳음
                        healthRedunctionWidth = 4
                        showSprite(fire)
                        for fireLabel in fireLabelList:
                            showLabel(fireLabel)
                    effectStartTime = pygame.time.get_ticks()   # effect가 시작된 시간.

        curTick = pygame.time.get_ticks()
        if (effect != 0 and (
                curTick - effectStartTime) // 1000 % 60 > EFFECT_TIME):  # 몬스터와 충돌로 인한 effect가 적용되는 시간이 다 지난 경우
            if effect == 1:
                hideSprite(dark)
                showLabel(hpLabel)
                itemLabel = makeLabel(" : " + str(player.itemNum) + " / " + str(TOTAL_ITEM_NUM), 30, 432, 120, "white", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.
                showLabel(itemLabel)
                for darkLabel in darkLabelList:
                    hideLabel(darkLabel)
            elif effect == 2:
                hideSprite(fire)
                for fireLabel in fireLabelList:
                    hideLabel(fireLabel)
                healthRedunctionWidth = 1
            effect = 0  # effect의 값을 0으로 바꿔서 이펙트 적용을 해제함

        if (int(beforeSecond) > int(curSecond) or int(beforeMinute) > int(curMinute)):  # 표시할 시간이 줄어들었을 때만 시간을 바꿈 (조건없이 while문을 반복하면서 시간을 바꿔주면 시간이 너무 깜빡거림)
            # 시간 update
            hideLabel(clockLabel) # 기존의 시간을 숨김
            if(int(curSecond) < 10) : curSecond = '0' + curSecond   #9초 이하에는 '0'을 붙여줌

            if(int(curSecond) < 10 and int(curMinute) == 0) :   # 남은 시간이 10초 이하라면, 시간을 표시하는 label의 색을 red로 표현
                clockLabel = makeLabel(curMinute + ":" + curSecond, 45, 290, 20, "red", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.
            else:
                clockLabel = makeLabel(curMinute + ":" + curSecond, 45, 290, 20, "white", FONT_FAMILY)  # 파라미터는 순서대로, initial text, fontsize, x, y, color, fontname, backgound를 나타낸다.

            showLabel(clockLabel) # 시간을 화면에 표시
            # 현재 시간을 갱신
            beforeMinute = curMinute
            beforeSecond = curSecond
            player.health -= healthRedunctionWidth  # 플레이어의 체력은 1초당 healthRedunctionWidth(체력 감소 폭)씩 감소 (몬스터와 충돌하여 감소 폭이 증가할 수 있다.)
            for i in range(0, healthRedunctionWidth):
                if (len(player.healthBarList) != 0):
                    hideSprite(player.healthBarList.pop()) # 플레이어의 체력이 감소하는 것을 표현하기 위해 감소폭만큼 체력바의 이미지를 가린다.

        for monster in monsterList :
            monster.moveMonster()

        if keyPressed("right"):
            scrollGameObj(player, frame, 0, -SCROLL_SPEED, 0, monsterList, obstacleList, itemList, effect)
        elif keyPressed("down"):
            scrollGameObj(player, frame, 1, 0, -SCROLL_SPEED, monsterList, obstacleList, itemList, effect)
        elif keyPressed("left"):
            scrollGameObj(player, frame, 2, SCROLL_SPEED, 0, monsterList, obstacleList, itemList, effect)
        elif keyPressed("up"):
            scrollGameObj(player, frame, 3, 0, SCROLL_SPEED, monsterList, obstacleList, itemList, effect)
        else:
            changeSpriteImage(player.character, 1 * PLAYER_TOTAL_FRAME + 5)  # 아무것도 누르지 않았을 땐 캐릭터가 앞을 보게함.

        updateDisplay()
        tick(120)

    for label in textboxGroup :    # 모든 라벨을 가림
        hideLabel(label)

    hideAll()   # 모든 sprite 숨김.
    if player.gameResult == LOSE_CODE:
        gameOverImage = makeSprite("./images/result/fail.png")
        showSprite(gameOverImage)
        pause(2000)

    hideAll()   # 모든 sprite 숨김.
    return player.gameResult
