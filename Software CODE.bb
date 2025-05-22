LeeCfgTxt = ReadFile("Autoplay/Docs/Config.txt")

WndCald = ReadLine$( LeeCfgTxt )
WidScrScn = ReadLine$( LeeCfgTxt )
HigScrScn  = ReadLine$( LeeCfgTxt )
WndModo = ReadLine$( LeeCfgTxt )
WndEfec = ReadLine$( LeeCfgTxt );Pantalla

Graphics3D WidScrScn,HigScrScn,0,WndModo
SetBuffer BackBuffer()
	AppTitle "Recorrido Virtual"
	CloseFile(LeeCfgTxt)

	Local CasoVentana=1	
	;________________________________________________________________________
frameTimer = CreateTimer(60)
	;________________________________________________________________________
IMG1 = LoadImage ("AutoPlay/Images/LoadStatic.png")
IMG2 = CopyImage(IMG1)
	
	DrawImage IMG1 ,WidScrScn/4-ImageWidth (IMG1)/2,HigScrScn/2-ImageHeight(IMG1)/2
	DrawImage IMG2 ,WidScrScn/4+WidScrScn/2-ImageWidth (IMG2)/2,HigScrScn/2-ImageHeight(IMG2)/2
	
	FreeImage IMG1: Flip
	FreeImage IMG2: Flip

;Cursor= LoadImage ("AutoPlay/Images/Cursor.png")
;Cursor2= LoadImage ("AutoPlay/Images/Cursor.png")
	HidePointer		
	
	;______________________SonidoFondo________
SndFon=LoadSound("AutoPlay/Audio/Fondo.mp3")
	LoopSound SndFon		
	;______________________Personaje__________	
Collisions 1,2,2,3

Luz=CreateLight(1)
	TurnEntity Luz,28,0,40

MyPlayer =CreatePivot()
	EntityRadius MyPlayer,0.2,0.8
	EntityType MyPlayer,1,True
	PositionEntity MyPlayer,0,2,-5
	;PositionEntity MyPlayer,2,1.2,4
	HideEntity MyPlayer
	
	;______________________Modelos Escena__________
Sky=LoadMesh( "AutoPlay/Models/Toon Sky.b3d")
	;HideEntity Sky
	;---
Limit=LoadMesh( "AutoPlay/Models/Toon Limit.b3d")
	EntityType Limit,2	
	;---

	;Scene=LoadAnimMesh( "AutoPlay/Models/Toon Castle.b3d")
	;EntityType Scene,2,True

;---------------------
Dim Scene(5)
For kt=1 To 5	
	Scene(kt)=LoadAnimMesh( "AutoPlay/Kotosh/Kotosh_"+kt+".b3d")
	EntityType Scene(kt),2,True
Next
	HideEntity Scene(4): HideEntity Scene(5)
;---------------------

player=LoadMesh( "AutoPlay/Models/Toon Player.b3d")
	 ShowEntity Scene(4): ShowEntity Scene(5)

	;______________________Camara__________
If WndEfec=0 Then
	ShowEntity MyPlayer
	Camera=CreateCamera(MyPlayer)
	;CameraClsColor Camera,0,0,0
	CameraRange camera,0.05,500
Else
	ShowEntity MyPlayer
	Camera=CreateCamera(MyPlayer)
	CameraViewport Camera,0,0,GraphicsWidth()/2,GraphicsHeight()
	;CameraClsColor Camera,50,12,150
	PositionEntity Camera,0.05,0,0
	CameraRange camera,0.1,500
	
	cam2=CreateCamera(MyPlayer)
	CameraViewport cam2,GraphicsWidth()/2,0,GraphicsWidth()/2,GraphicsHeight()
	;CameraClsColor cam2,100,12,50
	PositionEntity cam2,-0.05,0,0
	CameraRange cam2,0.1,500
;	
;	Vision=CreateSphere(8,Camera)
;	ScaleEntity Vision,0.01,0.01,0.01
;	PositionEntity Vision,0,0,5
;	PointEntity cam2,Vision
;	PointEntity Camera,Vision
EndIf

;	Menu=LoadAnimMesh( "AutoPlay/Models/Menu.b3d",MyPlayer)
;		PositionEntity Menu,0,.5,1

	;______________________VariablesIniciales_______
	CasoVentana=2

	MovZ#=0.04
	EstY#=EntityY(MyPlayer)
	GraV#=0.01
	
	Global mousespeedx# = 0.4
	Global mousespeedy# = 0.2
	Global playerspeed# = 0.8
	Global camerasmoothness# = 3
	Global mx#, my#, pitch#, yaw#, roll#,mj#

	tx#=50
	pr#=0
	Mipr=0

	mcar#=0.0
	mcab#=0.0
	Mipr=0
	pp=1
	Imprime=0
	AngCamY#=0.0
	posmouse#=0.0
;============================================================================================
;============================================================================================

While Not KeyDown(1)
	Select True
		Case CasoVentana=1	;>>CASO 1 Videos>> presionar Enter para saltar
			If EstadoVideo=0
				EstadoVideo=1
				EstadoVideo=OpenMovie("AutoPlay/Videos/Video2.avi")
			EndIf
			
			If EstadoVideo>0
				DrawMovie EstadoVideo,0,0,GraphicsWidth(),GraphicsHeight()
				If MoviePlaying(EstadoVideo)=0
					CloseMovie(EstadoVideo): CasoVentana=2: SonidoFon=PlaySound(SndFon):ChannelVolume SonidoFon,0.5
					EstadoVideo=0
				EndIf
			EndIf
			;---------------------------------------
			If KeyHit(28) Or JoyHit(12) Then CasoVentana=2: CloseMovie(EstadoVideo):EstadoVideo=0: SonidoFon=PlaySound(SndFon):ChannelVolume SonidoFon,0.5

	Case CasoVentana=2
			
		WaitTimer(frameTimer)
		;________________________________________________________________________

		TurnEntity Sky,0,0.02,0

		;___________________MouseMove________________________________________
		mx#=CurveValue(MouseXSpeed()*mousespeedx,mx,camerasmoothness)
		my#=CurveValue(MouseYSpeed()*mousespeedy,my,camerasmoothness)
		
		If WndEfec=0 Then
			MoveMouse GraphicsWidth()/2,GraphicsHeight()/2
			posmouse= GraphicsWidth()/2
		Else
			MoveMouse GraphicsWidth()/4,GraphicsHeight()/2
			posmouse= GraphicsWidth()/4
		End If
		
		pitch#=EntityPitch(MyPlayer)
		yaw#=EntityYaw(MyPlayer)
		pitch=pitch+my
		yaw=yaw-mx;-(2*(JoyXDir()))
		If pitch > 9  pitch = 9
		If pitch < -49 pitch = -49
	
		If JoyDown(7) yaw=yaw+2
		If JoyDown(8) yaw=yaw-2
		
		RotateEntity MyPlayer, 0,yaw,0

		If KeyHit(78) Then AngCamY=AngCamY+1
		If KeyHit(74) Then AngCamY=AngCamY-1
		
		If AngCamY>=20 Then AngCamY=20
		If AngCamY<=-20 Then AngCamY=-20
		
		If WndEfec=0 Then
			TurnEntity camera,pitch,0,0
		Else
			TurnEntity camera,pitch*2,0,0
			TurnEntity cam2,pitch*2,0,0
		;	RotateEntity camera,0,-AngCamY,0	
		;	RotateEntity cam2,0,AngCamY,0		
		End If		
		
		MovY#=EntityY(MyPlayer)-EstY
		EstY=EntityY(MyPlayer)
		
		If EntityCollided(MyPlayer,2) Then
			ContCol=CountCollisions(MyPlayer)
			ColFaceY= CollisionNY (MyPlayer,ContCol)
			If 	ColFaceY=1 Then
				Texto$="Saltar"	;habilitado para saltar
			Else
				Texto$="Pared"	;entonces no salta mas, porque choca con un muro
			EndIf
		Else
			Texto$="Aire" 		;No puede saltar 2 veces, porque esta en el aire, obveoooo
		EndIf
	
		If KeyHit(57) Or JoyHit(4) And Texto$="Saltar" Then MovY=.12; Barra Espaciadora
		
		If KeyHit(16)=True Or JoyHit(1)=True Then enable=1-enable ;letra Q agacharse
		If KeyHit(18)=True Or JoyHit(2)=True Then mizoom=1-mizoom ;letra e Zoom
	
		If enable=0
			If tx<50 tx=tx+1
			
			If WndEfec=0 Then
				PositionEntity camera,0,(tx*0.01),0
			Else
				PositionEntity camera,0,(tx*0.01),0
				PositionEntity cam2,0,(tx*0.01),0
			End If			
		Else
			If tx>-50 tx=tx-1
			
			If WndEfec=0 Then
				PositionEntity camera,0,(tx*0.01),0
			Else
				PositionEntity camera,0,(tx*0.01),0
				PositionEntity cam2,0,(tx*0.01),0
			End If
		End If
	
		If mizoom=0
			If WndEfec=0 Then CameraZoom camera,1
			If WndEfec=1 Then CameraZoom camera,1:CameraZoom cam2,1	
		Else
			If WndEfec=0 Then CameraZoom camera,4
			If WndEfec=1 Then CameraZoom camera,4:CameraZoom cam2,4
		End If
		
		MoveEntity MyPlayer,0,MovY-GraV,0
	
		If KeyDown(30) Or KeyDown(203) Or JoyX()=-1 MoveEntity MyPlayer,-0.02,0,0;Izquierda
		If KeyDown(32) Or KeyDown(205) Or JoyX()=1  MoveEntity MyPlayer,0.02,0,0	;Derecha
		;If KeyDown(30) Or KeyDown(203) Or JoyDown(7) MoveEntity MyPlayer,-0.02,0,0;Izquierda
		;If KeyDown(32) Or KeyDown(205) Or JoyDown(8)  MoveEntity MyPlayer,0.02,0,0	;Derecha
				
		If KeyDown(17) Or KeyDown(200) Or JoyY()=-1 MoveEntity MyPlayer,0,0,MovZ	;Adelante
		If KeyDown(31) Or KeyDown(208) Or JoyY()=1 MoveEntity MyPlayer,0,0,-MovZ;Atras
		
		
		;If KeyDown(78)	MoveEntity Vision,0,0,0.01:PointEntity cam2,Vision: PointEntity Camera,Vision
		;If KeyDown(74)	MoveEntity Vision,0,0,-0.01:PointEntity cam2,Vision: PointEntity Camera,Vision
		
		RenderWorld
		UpdateWorld
		;	DrawImage cursor,posmouse,GraphicsHeight()/2
		;	DrawImage cursor2,posmouse+GraphicsWidth()/2,GraphicsHeight()/2
		Default
	End Select			
Flip: Wend
End

Function CurveValue#(newvalue#,oldvalue#,increments )
	If increments>1 oldvalue#=oldvalue#-(oldvalue#-newvalue#)/increments
	If increments<=1 oldvalue=newvalue
	Return oldvalue#
End Function
