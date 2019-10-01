// Generated Code
// Please DO NOT EDIT


package sifive.blocks.PWM

import chisel3._
// import chisel3.{withClockAndReset, _}
import chisel3.util._
import chisel3.experimental._

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.amba.axi4._
import freechips.rocketchip.amba.apb._
import freechips.rocketchip.amba.ahb._
import freechips.rocketchip.interrupts._
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.tilelink._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.regmapper._

import sifive.skeleton._
import sifive.blocks.util.{NonBlockingEnqueue, NonBlockingDequeue}



class PWMBlackBoxIO(

) extends Bundle {
  val PCLK = Input(Bool())
  val PRESETn = Input(Bool())
  val PENABLE = Input(Bool())
  val PSELPTC = Input(Bool())
  val PADDR = Input(UInt((7).W))
  val PWRITE = Input(Bool())
  val PWDATA = Input(UInt((32).W))
  val PRDATA = Output(UInt((32).W))
  val INTptc0 = Output(Bool())
  val INTptc1 = Output(Bool())
  val INTptc2 = Output(Bool())
  val INTptc3 = Output(Bool())
  val INTptc4 = Output(Bool())
  val INTptc5 = Output(Bool())
  val capt0_event = Input(Bool())
  val capt1_event = Input(Bool())
  val capt2_event = Input(Bool())
  val capt3_event = Input(Bool())
  val capt4_event = Input(Bool())
  val capt5_event = Input(Bool())
  val PWM_OUT0 = Output(Bool())
  val PWM_OUT1 = Output(Bool())
  val PWM_OUT2 = Output(Bool())
  val PWM_OUT3 = Output(Bool())
  val PWM_OUT4 = Output(Bool())
  val PWM_OUT5 = Output(Bool())
}

class PWM(

) extends BlackBox(Map(

)) with HasBlackBoxResource {
  val io = IO(new PWMBlackBoxIO(

  ))
// setResource("top.v")
}

case class PWMParams(
  ctrlParams: PctrlParams,
  irqParams: PirqParams,
  cacheBlockBytes: Int
)

// busType: APB4, mode: slave
// busType: interrupts, mode: master

class LPWMBase(c: PWMParams)(implicit p: Parameters) extends LazyModule {
  val device = new SimpleDevice("PWM", Seq("sifive,PWM-v0"))



  val ctrlNode = APBSlaveNode(Seq(
    APBSlavePortParameters(
      slaves = Seq(APBSlaveParameters(
        address = List(AddressSet(c.ctrlParams.base, 0x7fL)),
        // resources
        // regionType
        executable = false,
        // nodePath
        supportsWrite = true,
        supportsRead  = true
        // device
      )),
      beatBytes = 32 / 8
    )
  ))

  val irqNode = IntSourceNode(IntSourcePortSimple(num = 6))

  val ioBridgeSource = BundleBridgeSource(() => new PWMBlackBoxIO(

  ))

  class LPWMBaseImp extends LazyRawModuleImp(this) {
    val blackbox = Module(new PWM(

    ))
    // interface wiring 2
    // busType: APB4, mode: slave

    // port wiring
    blackbox.io.PCLK := ioBridgeSource.bundle.PCLK
    blackbox.io.PRESETn := ioBridgeSource.bundle.PRESETn
    blackbox.io.PENABLE := ioBridgeSource.bundle.PENABLE
    blackbox.io.PSELPTC := ioBridgeSource.bundle.PSELPTC
    blackbox.io.PADDR := ioBridgeSource.bundle.PADDR
    blackbox.io.PWRITE := ioBridgeSource.bundle.PWRITE
    blackbox.io.PWDATA := ioBridgeSource.bundle.PWDATA
    ioBridgeSource.bundle.PRDATA := blackbox.io.PRDATA
    ioBridgeSource.bundle.INTptc0 := blackbox.io.INTptc0
    ioBridgeSource.bundle.INTptc1 := blackbox.io.INTptc1
    ioBridgeSource.bundle.INTptc2 := blackbox.io.INTptc2
    ioBridgeSource.bundle.INTptc3 := blackbox.io.INTptc3
    ioBridgeSource.bundle.INTptc4 := blackbox.io.INTptc4
    ioBridgeSource.bundle.INTptc5 := blackbox.io.INTptc5
    blackbox.io.capt0_event := ioBridgeSource.bundle.capt0_event
    blackbox.io.capt1_event := ioBridgeSource.bundle.capt1_event
    blackbox.io.capt2_event := ioBridgeSource.bundle.capt2_event
    blackbox.io.capt3_event := ioBridgeSource.bundle.capt3_event
    blackbox.io.capt4_event := ioBridgeSource.bundle.capt4_event
    blackbox.io.capt5_event := ioBridgeSource.bundle.capt5_event
    ioBridgeSource.bundle.PWM_OUT0 := blackbox.io.PWM_OUT0
    ioBridgeSource.bundle.PWM_OUT1 := blackbox.io.PWM_OUT1
    ioBridgeSource.bundle.PWM_OUT2 := blackbox.io.PWM_OUT2
    ioBridgeSource.bundle.PWM_OUT3 := blackbox.io.PWM_OUT3
    ioBridgeSource.bundle.PWM_OUT4 := blackbox.io.PWM_OUT4
    ioBridgeSource.bundle.PWM_OUT5 := blackbox.io.PWM_OUT5
    // interface alias
    val ctrl0 = ctrlNode.in(0)._1
    val irq0 = irqNode.out(0)._1
    // interface wiring
    // wiring for ctrl of type APB4
    // -> {"prdata":"dataWidth","pwrite":1,"penable":1,"psel":1,"pready":-1,"pslverr":1,"paddr":"addrWidth","pwdata":"dataWidth","pprot":3}ctrl0.prdata := blackbox.io.PRDATA
    blackbox.io.PWRITE := ctrl0.pwrite
    blackbox.io.PENABLE := ctrl0.penable
    // PSEL
    ctrl0.pready := true.B // PREADY
    // PSLVERR
    blackbox.io.PADDR := ctrl0.paddr
    blackbox.io.PWDATA := ctrl0.pwdata
    // PPROT

    // wiring for irq of type interrupts
    // ["INTptc0","INTptc1","INTptc2","INTptc3","INTptc4","INTptc5"]
  }
  lazy val module = new LPWMBaseImp
}


case class PctrlParams(
  base: BigInt,
  executable: Boolean = false,
  maxFifoBits: Int = 2,
  maxTransactions: Int = 1,
  axi4BufferParams: AXI4BufferParams = AXI4BufferParams(),
  tlBufferParams: TLBufferParams = TLBufferParams()
)


case class PirqParams()


case class NPWMTopParams(
  blackbox: PWMParams
) {
  def setBurstBytes(x: Int): NPWMTopParams = this.copy()
}

object NPWMTopParams {
  def defaults(
    ctrl_base: BigInt,
    cacheBlockBytes: Int
  ) = NPWMTopParams(
    blackbox = PWMParams(
      ctrlParams = PctrlParams(base = ctrl_base),
      irqParams = PirqParams(),
      cacheBlockBytes = cacheBlockBytes
    )
  )
}

class NPWMTopBase(c: NPWMTopParams)(implicit p: Parameters) extends SimpleLazyModule {
  val imp = LazyModule(new LPWM(c.blackbox))

// no channel node

  val ctrlNode: APBSlaveNode = imp.ctrlNode

  def getctrlNodeTLAdapter(): TLInwardNode = {(
    ctrlNode
      := TLToAPB(false)
      := TLBuffer()
      := TLFragmenter((32 / 8), c.blackbox.cacheBlockBytes, holdFirstDeny=true)
  )}


  val irqNode: IntSourceNode = imp.irqNode
}

object NPWMTopBase {
  def attach(c: NPWMTopParams)(bap: BlockAttachParams): NPWMTop = {
    implicit val p: Parameters = bap.p
    val PWM_top = LazyModule(new NPWMTop(c))
    // no channel attachment
    bap.pbus.coupleTo("PWM_apb") { PWM_top.getctrlNodeTLAdapter() := TLWidthWidget(bap.pbus) := _ }
    bap.ibus := PWM_top.irqNode
    PWM_top
  }
}

class WithPWMTopBase (
  ctrl_base: BigInt
) extends Config((site, here, up) => {
  case BlockDescriptorKey =>
    BlockDescriptor(
      name = "PWM",
      place = NPWMTop.attach(NPWMTopParams.defaults(
        ctrl_base = ctrl_base,
        cacheBlockBytes = site(CacheBlockBytes)
      ))
    ) +: up(BlockDescriptorKey, site)
})
