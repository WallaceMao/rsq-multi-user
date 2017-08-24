import com.rishiqing.mid.simulation.Computer

Computer.metaClass.compute = { boolean str -> new Date() }

Computer c = new Computer()
println c.compute(true)
